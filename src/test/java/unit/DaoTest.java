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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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

        @Test
        void currencyInfo() {
            @Cleanup Session session = HibernateSessionFactory.getSessionFactory()
                    .openSession();
            session.beginTransaction();
            ItemsEntity itemsEntity = session.get(ItemsEntity.class, 2l);
            itemsEntity.getCurrencyInfos()
                    .add(CurrencyInfo.of(1000.00, CurrencyEnum.$)
                    );
            itemsEntity.getCurrencyInfos()
                    .add(CurrencyInfo.of(89_000.00, CurrencyEnum.₽)
                    );
            session.getTransaction()
                    .commit();

        }

        @ParameterizedTest
        @DisplayName("если orphanRemoval=true, то при удалении комментария из топика он удаляется из базы")
        @MethodSource("unit.DaoTest#getArgumentsForItemsTestAndPersonalAccount")
        void givenOrphanRemovalTrue_whenRemoveSellHistoryEntityFromPhoneOrders_thenItRemovedFromDatabase(ItemsEntity itemsEntity, PersonalAccountEntity personalAccountEntity) {
            @Cleanup Session session = HibernateSessionFactory.getSessionFactory()
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
}
