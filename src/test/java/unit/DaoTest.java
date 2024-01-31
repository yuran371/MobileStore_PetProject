package unit;

import dao.ItemsDao;
import dao.PersonalAccountDao;
import dao.SellHistoryDao;
import entity.*;
import extentions.PersonalAccountParameterResolver;
import extentions.SellHistoryParameterResolver;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.HibernateTestUtil;
import utlis.HibernateSessionFactory;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
public class DaoTest {
    @Nested
    @Tag(value = "ItemsDao")
    class Items {
        ItemsDao itemsDao = ItemsDao.getInstance();

        @ParameterizedTest
        @MethodSource("unit.DaoTest#getListOfItemsOfArguments")
        void countItems_countIs3_True(List<ItemsEntity> list) {
            @Cleanup Session session = HibernateTestUtil.getSessionFactory()
                    .openSession();
            session.beginTransaction();
            for (ItemsEntity item : list) {
                session.persist(item);
            }
            List<ItemsEntity> offsetLimitList = itemsDao.findItemsLimitOffsetViaHibernate(3, 0, session);

            session.getTransaction()
                    .commit();
            assertThat(offsetLimitList.size()).isEqualTo(3);
            // Написать тест: смещение списка и сравнение полученных items
        }

        @ParameterizedTest
        @MethodSource("unit.DaoTest#getArgumentsForItemsTest")
        void currencyInfo(ItemsEntity itemsEntity) {
            @Cleanup Session session = HibernateTestUtil.getSessionFactory()
                    .openSession();
            session.beginTransaction();
            session.persist(itemsEntity);
            ItemsEntity item = session.get(ItemsEntity.class, 1l);
            item.getCurrencyInfos()
                    .add(CurrencyInfo.of(1000.00, CurrencyEnum.$)
                    );
            item.getCurrencyInfos()
                    .add(CurrencyInfo.of(89_000.00, CurrencyEnum.₽)
                    );
            System.out.println(item.getCurrencyInfos());
            session.getTransaction()
                    .commit();
        }

        @ParameterizedTest
        @DisplayName("если orphanRemoval=true, то при удалении комментария из топика он удаляется из базы")
        @MethodSource("unit.DaoTest#getArgumentsForItemsTestAndPersonalAccount")
        void givenOrphanRemovalTrue_whenRemoveSellHistoryEntityFromPhoneOrders_thenItRemovedFromDatabase(ItemsEntity itemsEntity, PersonalAccountEntity personalAccountEntity) {
            @Cleanup Session session = HibernateTestUtil.getSessionFactory()
                    .openSession();
            session.beginTransaction();
            itemsDao.insertViaHibernate(itemsEntity, session);
            session.persist(personalAccountEntity);
            Long itemId = itemsEntity.getItemId();
            List<SellHistoryEntity> collectOfSellHistoryEntity =
                    getArgumentForSellHistory().map(arguments -> (SellHistoryEntity) arguments.get()[0])
                            .collect(Collectors.toList());
            collectOfSellHistoryEntity.stream()
                    .forEach(sellHistoryEntity -> {
                        itemsEntity.addPhoneOrder(sellHistoryEntity);
                        personalAccountEntity.addPurchase(sellHistoryEntity);
                        session.persist(sellHistoryEntity);
                    });
            /*
             * 64-68стр Нужны были для "обновления" данных (актуализации List). После добавления 57стр deprecated
             * */
//            session.getTransaction()
//                    .commit();
//            session.detach(itemsEntity);
//            session.beginTransaction();
//            ItemsEntity itemsEntity1 = session.get(itemsEntity.getClass(), itemId);
            itemsEntity.getPhoneOrders()
                    .remove(0);
            itemsEntity.removePhoneOrder(itemsEntity.getPhoneOrders()
                    .get(0));   //  Тестируем удаление sellHistoryEntity с orphanRemoval = true
            personalAccountEntity.removePurchase(personalAccountEntity.getPhonePurchases()
                    .get(0));   //  Тестируем удаление sellHistoryEntity с orphanRemoval = true

            SellHistoryEntity sellHistoryEntity = collectOfSellHistoryEntity.get(0);
            Long sellId = sellHistoryEntity
                    .getSellId();
            session.remove(sellHistoryEntity);
            SellHistoryEntity sellHistoryEntityIsNull = session.get(sellHistoryEntity.getClass(), sellId);  //
            // sellHistoryEntity должен быть null после session.remove(sellHistoryEntity);
            session.remove(itemsEntity);
            session.remove(personalAccountEntity);
            session.flush();
            session.getTransaction()
                    .commit();
            assertThat(sellHistoryEntityIsNull).isNull();   //  Проверка, удалился ли SellHistory из таблицы

            log.info("Just added: {} {} {} {} qt: {}", itemsEntity.getBrand(), itemsEntity.getModel(),
                    itemsEntity.getPrice(), itemsEntity.getCurrency(), itemsEntity.getQuantity());
        }
    }


    @Nested
    @TestInstance(value = Lifecycle.PER_CLASS)
    @Tag(value = "PersonalAccountDao")
    @ExtendWith(value = {PersonalAccountParameterResolver.class})
    class PersonalAccount {

        private PersonalAccountDao instance;

        public PersonalAccount(PersonalAccountDao instance) {
            this.instance = instance;
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#getArgumentForPersonalAccountTest")
        void insert_NewUser_notNull(PersonalAccountEntity account) {
            @Cleanup SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
            @Cleanup Session session = sessionFactory.openSession();
            sessionFactory.close();

            Optional<Long> insert = instance.insert(account);
            assertThat(insert).isNotEmpty();
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#getArgumentForPersonalAccountTest")
        void insertMethodAddUserReturnsUserIdFromDB(PersonalAccountEntity account) {
            Optional<Long> insert = instance.insert(account);
            assertThat(insert).isNotEmpty();
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#getArgumentForPersonalAccountTest")
        void get_OrdersList_ConsistAllOrders(PersonalAccountEntity account) {
            List<SellHistoryEntity> sellHistoryEntityList = DaoTest.getArgumentsForSellHistory()
                    .flatMap(arguments -> Arrays.stream(arguments.get()))
                    .map(objectOfEntity -> (SellHistoryEntity) objectOfEntity)
                    .collect(Collectors.toList());
            @Cleanup SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
            @Cleanup Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(account);
            Long accountId = account.getAccountId();
            sellHistoryEntityList.stream()
                    .forEach(sellHistoryEntity -> {
                        sellHistoryEntity.setUser(account);
                        session.persist(sellHistoryEntity);
                    });
            session.detach(account);
            session.getTransaction()
                    .commit();
            session.beginTransaction();
            PersonalAccountEntity personalAccountEntity = session.get(PersonalAccountEntity.class, accountId);
            assertThat(personalAccountEntity.getOrders()
                    .size()).isEqualTo(sellHistoryEntityList.size());
            sellHistoryEntityList.stream()
                    .forEach(sellHistoryEntity -> session.remove(sellHistoryEntity));
            session.remove(personalAccountEntity);
            session.getTransaction()
                    .commit();
        }

    }

    @Nested
    @TestInstance(value = Lifecycle.PER_METHOD)
    @Tag(value = "PersonalAccountDao")
    @ExtendWith({SellHistoryParameterResolver.class})
    class SellHistoryTest {

        private final SellHistoryDao instance;

        public SellHistoryTest(SellHistoryDao instance) {
            this.instance = instance;
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#getArgumentsForSellHistory")
        void insert_NewSell_notNull(SellHistoryEntity entity) {
            Optional<Long> id = instance.insert(entity);
            assertThat(id).isNotEmpty();
            @Cleanup SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
            @Cleanup Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            id.ifPresent(sellId -> {
                SellHistoryEntity sellHistoryEntity = session.get(SellHistoryEntity.class, sellId);
                session.remove(sellHistoryEntity);
                transaction.commit();
            });
        }

    }

    public static Stream<Arguments> getArgumentsForSellHistory() {
        return Stream.of(Arguments.of(SellHistoryEntity.builder()
                        .sellDate(OffsetDateTime.now())
                        .user(PersonalAccountEntity.builder()
                                .accountId(30L)
                                .build())
                        .itemId(ItemsEntity.builder()
                                .itemId(1L)
                                .build())
                        .quantity(2)
                        .build(),
                SellHistoryEntity.builder()
                        .sellDate(OffsetDateTime.now())
                        .user(PersonalAccountEntity.builder()
                                .accountId(30L)
                                .build())
                        .itemId(ItemsEntity.builder()
                                .itemId(2L)
                                .build())
                        .quantity(3)
                        .build(),
                SellHistoryEntity.builder()
                        .sellDate(OffsetDateTime.now())
                        .user(PersonalAccountEntity.builder()
                                .accountId(30L)
                                .build())
                        .itemId(ItemsEntity.builder()
                                .itemId(2L)
                                .build())
                        .quantity(10)
                        .build()));
    }

    public static Stream<Arguments> getArgumentForPersonalAccountTest() {
        return Stream.of(Arguments.of(PersonalAccountEntity.builder()
                .address("no address")
                .birthday(LocalDate.now()
                        .minusYears(20))
                .city("no city")
                .country(Country.KAZAKHSTAN)
                .email("noemail@email.ru")
                .gender(Gender.MALE)
                .image("")
                .name("Sasha")
                .password("123")
                .phoneNumber("+79214050505")
                .surname("nonamich")
                .build()));

    }

    public static Stream<Arguments> getArgumentForSellHistory() {
        return Stream.of(Arguments.of(SellHistoryEntity.builder()
                .sellDate(OffsetDateTime.now())
                .user(PersonalAccountEntity.builder()
                        .accountId(2L)
                        .build())
                .itemId(ItemsEntity.builder()
                        .itemId(2l)
                        .build())
                .quantity(2)
                .build()
        ));
    }

    public static Stream<Arguments> getArgumentsForItemsTestAndPersonalAccount() {
        return Stream.of(Arguments.of(ItemsEntity.builder()

                        .model("pixel a5")
                        .brand(BrandEnum.Google)
                        .attributes("128gb green")
                        .price(999.99)
                        .currency(CurrencyEnum.$)
                        .quantity(57)
                        .build(),
                PersonalAccountEntity.builder()
                        .image("")
                        .name("Artem")
                        .surname("Eranov")
                        .email("sobaka@mail.ru")
                        .birthday(LocalDate.of(1990, 12, 12))
                        .city("Oren")
                        .address("Pushkina")
                        .country(Country.KAZAKHSTAN)
                        .gender(Gender.MALE)
                        .phoneNumber("+79553330987")
                        .password("1499")
                        .build()
        ));
    }

    public static Stream<Arguments> getArgumentsForItemsTest() {
        return Stream.of(Arguments.of(ItemsEntity.builder()
                .model("pixel a5")
                .brand(BrandEnum.Google)
                .attributes("128gb green")
                .price(999.99)
                .currency(CurrencyEnum.$)
                .quantity(57)
                .build()));
    }

    public static Stream<Arguments> getListOfItemsOfArguments() {
        return Stream.of(Arguments.of(List.of(ItemsEntity.builder()
                        .model("iPhone 14")
                        .brand(BrandEnum.Apple)
                        .attributes("128gb black")
                        .price(89_990.00)
                        .currency(CurrencyEnum.₽)
                        .quantity(83)
                        .build(),
                ItemsEntity.builder()
                        .model("iPhone 11")
                        .brand(BrandEnum.Apple)
                        .attributes("64gb red")
                        .price(79_999.99)
                        .currency(CurrencyEnum.₽)
                        .quantity(55)
                        .build(),
                ItemsEntity.builder()
                        .model("iPhone 15 Pro Max")
                        .brand(BrandEnum.Apple)
                        .attributes("1024gb white")
                        .price(215_999.99)
                        .currency(CurrencyEnum.₽)
                        .quantity(14)
                        .build(),
                ItemsEntity.builder()
                        .model("iPhone 14 Pro Max")
                        .brand(BrandEnum.Apple)
                        .attributes("512gb spaceGrey")
                        .price(36_999.99)
                        .currency(CurrencyEnum.₽)
                        .quantity(99)
                        .build(),
                ItemsEntity.builder()
                        .model("Redmi A2+")
                        .brand(BrandEnum.Xiaomi)
                        .attributes("128gb black")
                        .price(30_999.99)
                        .currency(CurrencyEnum.₽)
                        .quantity(114)
                        .build(),
                ItemsEntity.builder()
                        .model("13T")
                        .brand(BrandEnum.Xiaomi)
                        .attributes("64gb black")
                        .price(8_999.99)
                        .currency(CurrencyEnum.₽)
                        .quantity(223)
                        .build(),
                ItemsEntity.builder()
                        .model("Galaxy S21 FE")
                        .brand(BrandEnum.Samsung)
                        .attributes("128gb grey")
                        .price(28_999.99)
                        .currency(CurrencyEnum.₽)
                        .quantity(99)
                        .build(),
                ItemsEntity.builder()
                        .model("Galaxy S23 Ultra")
                        .brand(BrandEnum.Samsung)
                        .attributes("256gb white")
                        .price(119_999.99)
                        .currency(CurrencyEnum.₽)
                        .quantity(8)
                        .build(),
                ItemsEntity.builder()
                        .model("Galaxy A04")
                        .brand(BrandEnum.Samsung)
                        .attributes("8gb black")
                        .price(5_999.99)
                        .currency(CurrencyEnum.₽)
                        .quantity(99)
                        .build()
        )));
    }
}
