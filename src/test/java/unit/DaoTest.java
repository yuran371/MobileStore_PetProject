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
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.HibernateTestUtil;
import utlis.HibernateSessionFactory;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static entity.Attributes.BrandEnum.*;
import static entity.Attributes.InternalMemoryEnum.*;
import static entity.Attributes.OperatingSystemEnum.ANDROID;
import static entity.Attributes.OperatingSystemEnum.IOS;
import static entity.Attributes.RamEnum.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


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
            List<ItemsEntity> offsetLimitList = itemsDao.findItemsLimitOffsetViaQuerydsl(3, 0, session);

            session.getTransaction()
                    .commit();
            assertThat(offsetLimitList.size()).isEqualTo(3);
            // Написать тест: смещение списка и сравнение полученных items
        }

        @ParameterizedTest
        @MethodSource("unit.DaoTest#getListOfItemsOfArguments")
        void offsetOnSecondPage_compareTwoEntities_True(List<ItemsEntity> list) {
            @Cleanup Session session = HibernateTestUtil.getSessionFactory()
                    .openSession();
            session.beginTransaction();
            for (ItemsEntity item : list) {
                session.persist(item);
            }
            List<ItemsEntity> offsetLimitList = itemsDao.findItemsLimitOffsetViaQuerydsl(3, 1, session);
            ItemsEntity itemOnSecondPage = offsetLimitList.get(0);
            System.out.println(itemOnSecondPage);
            session.getTransaction()
                    .commit();
            ItemsEntity itemExpected = ItemsEntity.builder()
                    .model("iPhone 14 Pro Max")
                    .brand(Attributes.BrandEnum.Apple)
                    .attributes("512gb spaceGrey")
                    .price(36_999.99)
                    .currency(CurrencyEnum.₽)
                    .quantity(99)
                    .build();
            assertThat(itemOnSecondPage).isEqualTo(itemExpected);
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
        @MethodSource("unit.DaoTest#argumentsForItemsTestAndPersonalAccount")
        void givenOrphanRemovalTrue_whenRemoveSellHistoryEntityFromPhoneOrders_thenItRemovedFromDatabase(ItemsEntity itemsEntity, PersonalAccountEntity personalAccountEntity) {
            @Cleanup Session session = HibernateTestUtil.getSessionFactory()
                    .openSession();
            session.beginTransaction();
            itemsDao.insertViaHibernate(itemsEntity, session);
            session.persist(personalAccountEntity);
            Long itemId = itemsEntity.getId();
//            List<SellHistoryEntity> collectOfSellHistoryEntity =
//                //    getArgumentForSellHistory().map(arguments -> (SellHistoryEntity) arguments.get()[0])
//                //            .collect(Collectors.toList());
//            collectOfSellHistoryEntity.stream()
//                    .forEach(sellHistoryEntity -> {
//                        itemsEntity.addPhoneOrder(sellHistoryEntity);
//                        personalAccountEntity.addPurchase(sellHistoryEntity);
//                        session.persist(sellHistoryEntity);
//                    });
            /*
             * 64-68стр Нужны были для "обновления" данных (актуализации List). После добавления 57стр deprecated
             * */
//            session.getTransaction()
//                    .commit();
//            session.detach(itemsEntity);
//            session.beginTransaction();
//            ItemsEntity itemsEntity1 = session.get(itemsEntity.getClass(), itemId);
//            itemsEntity.getPhoneOrders()
//                    .remove(0);
//            itemsEntity.removePhoneOrder(itemsEntity.getPhoneOrders()
//                    .get(0));   //  Тестируем удаление sellHistoryEntity с orphanRemoval = true
//            personalAccountEntity.removePurchase(personalAccountEntity.getPhonePurchases()
//                    .get(0));   //  Тестируем удаление sellHistoryEntity с orphanRemoval = true
//
//            SellHistoryEntity sellHistoryEntity = collectOfSellHistoryEntity.get(0);
//            Long sellId = sellHistoryEntity
//                    .getSellId();
//            session.remove(sellHistoryEntity);
//            SellHistoryEntity sellHistoryEntityIsNull = session.get(sellHistoryEntity.getClass(), sellId);  //
//            // sellHistoryEntity должен быть null после session.remove(sellHistoryEntity);
//            session.remove(itemsEntity);
//            session.remove(personalAccountEntity);
//            session.flush();
//            session.getTransaction()
//                    .commit();
//            assertThat(sellHistoryEntityIsNull).isNull();   //  Проверка, удалился ли SellHistory из таблицы

            log.info("Just added: {} {} {} {} qt: {}", itemsEntity.getBrand(), itemsEntity.getModel(),
                     itemsEntity.getPrice(), itemsEntity.getCurrency(), itemsEntity.getQuantity());
        }
    }


    @Nested
    @TestInstance(PER_CLASS)
    @Tag(value = "PersonalAccountDao")
    @ExtendWith(value = {PersonalAccountParameterResolver.class})
    class PersonalAccount {

        private PersonalAccountDao personalAccountDao;
        private SessionFactory entityManager = HibernateTestUtil.getSessionFactory();

        public PersonalAccount(PersonalAccountDao instance) {
            this.personalAccountDao = instance;
        }

        @AfterAll
        public void closeTestSessionFactory() {
            entityManager.close();
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsPersonalAccount")
        void insert_NewUser_notNull(PersonalAccountEntity account) {
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsPersonalAccount")
        void get_OrdersList_ConsistAllOrders(PersonalAccountEntity account) {
            List<SellHistoryEntity> sellHistoryEntityList = getSellHistoryEntities(3);
            @Cleanup SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
            @Cleanup Session session = sessionFactory.openSession(); session.beginTransaction();
            session.persist(account); Long accountId = account.getId();
            sellHistoryEntityList.stream().forEach(sellHistoryEntity -> {
                sellHistoryEntity.setUser(account); session.persist(sellHistoryEntity);
            }); session.detach(account); session.getTransaction().commit(); session.beginTransaction();
            PersonalAccountEntity personalAccountEntity = session.get(PersonalAccountEntity.class, accountId);
            assertThat(personalAccountEntity.getOrders().size()).isEqualTo(sellHistoryEntityList.size());
            sellHistoryEntityList.stream().forEach(sellHistoryEntity -> session.remove(sellHistoryEntity));
            session.remove(personalAccountEntity); session.getTransaction().commit();
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsPersonalAccount")
        void validateAuth_validUser_returnUser(PersonalAccountEntity account) {
            @Cleanup Session session = entityManager.openSession();
            DaoTest.persistEntity(account, session);
            Optional<PersonalAccountEntity> personalAccountEntity = personalAccountDao
                    .validateAuth(account.getEmail(), account.getPassword(), session);
            assertThat(personalAccountEntity.get()).isNotNull();
            assertThat(personalAccountEntity.get().getEmail()).isEqualTo(account.getEmail());
            assertThat(personalAccountEntity.get().getPassword()).isEqualTo(account.getPassword());
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsPersonalAccount")
        void getByEmail_validUser_returnUser(PersonalAccountEntity account) {
            @Cleanup Session session = entityManager.openSession();
            DaoTest.persistEntity(account, session);
            Optional<PersonalAccountEntity> personalAccountEntity = personalAccountDao
                    .getByEmail(account.getEmail(), session);
            assertThat(personalAccountEntity.get()).isNotNull();
            assertThat(personalAccountEntity.get().getId()).isEqualTo(account.getId());
            assertThat(personalAccountEntity.get().getEmail()).isEqualTo(account.getEmail());
            assertThat(personalAccountEntity.get().getPassword()).isEqualTo(account.getPassword());
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsPersonalAccount")
        void checkDiscount_premiumUser_returnDiscount(PersonalAccountEntity account) {
            @Cleanup Session session = entityManager.openSession();
            PremiumUserEntity premiumUserEntity = new PremiumUserEntity(account, Discount.FIVE_PERCENT);
            DaoTest.persistEntity(premiumUserEntity, session);
            Optional<Discount> discount = personalAccountDao.checkDiscount(premiumUserEntity.getId(), session);
            assertThat(discount.get()).isEqualTo(Discount.FIVE_PERCENT);
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsPersonalAccount")
        void checkDiscount_notPremiumUser_returnNull(PersonalAccountEntity account) {
            @Cleanup Session session = entityManager.openSession();
            DaoTest.persistEntity(account, session);
            Optional<Discount> discount = personalAccountDao.checkDiscount(account.getId(), session);
            assertThat(discount).isEmpty();
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsPersonalAccount")
        void getAllBoughtPhones_havePhones_returnList(PersonalAccountEntity account) {
            List<SellHistoryEntity> sellHistoryEntities = DaoTest.getSellHistoryEntities(3);
            List<ItemsEntity> itemsEntities = DaoTest.getItemsEntities(3);
            @Cleanup Session session = entityManager.openSession();
            DaoTest.persistEntity(account, session);
            DaoTest.persistEntitiesList(itemsEntities, session);
            for (int i = 0; i < sellHistoryEntities.size(); i++) {
                SellHistoryEntity sellHistoryEntity = sellHistoryEntities.get(i);
                sellHistoryEntity.setUser(account);
                sellHistoryEntity.setItemId(itemsEntities.get(i));
            }
            DaoTest.persistEntitiesList(sellHistoryEntities, session);
            List<ItemsEntity> allBoughtPhones = personalAccountDao.getAllBoughtPhones(account.getId(), session);
            assertThat(allBoughtPhones.size()).isEqualTo(itemsEntities.size());
            assertThat(allBoughtPhones).extracting("id").contains(itemsEntities.get(0).getId(),
                                                                  itemsEntities.get(1).getId(),
                                                                  itemsEntities.get(2).getId());
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsPersonalAccount")
        void getAllBoughtPhones_noPhones_returnEmptyList(PersonalAccountEntity account) {
            @Cleanup Session session = entityManager.openSession();
            DaoTest.persistEntity(account, session);
            List<ItemsEntity> allBoughtPhones = personalAccountDao.getAllBoughtPhones(account.getId(), session);
            assertThat(allBoughtPhones).isEmpty();
        }

        @Test
        void getTopTenMostSpenders_haveUsers_returnTop() {
            List<PersonalAccountEntity> accounts = DaoTest.getPersonalAccountEntities(3);
            List<ItemsEntity> items = DaoTest.getItemsEntities(10);
            List<SellHistoryEntity> sellHistoryEntities = DaoTest.getSellHistoryEntities(10);
            @Cleanup Session session = entityManager.openSession();
            DaoTest.persistEntitiesList(accounts, session);
            DaoTest.persistEntitiesList(items, session);
            for (SellHistoryEntity entity : sellHistoryEntities) {
                Random random = new Random();
                entity.setUser(accounts.get(random.nextInt(0, accounts.size())));
                entity.setItemId(items.get(random.nextInt(0, items.size())));
            }
            DaoTest.persistEntitiesList(sellHistoryEntities, session);
            List<Object[]> topTenMostSpenders = personalAccountDao.getTopTenMostSpenders(session);
            for (int i = 1; i < topTenMostSpenders.size(); i++) {
                Double spender = (Double) topTenMostSpenders.get(i - 1)[1];
                Double nextSpender = (Double) topTenMostSpenders.get(i)[1];
                assertThat(spender).isGreaterThan(nextSpender);
            }
        }

    }

    @Nested
    @TestInstance(value = PER_CLASS)
    @Tag(value = "SellHistoryDao")
    @ExtendWith({SellHistoryParameterResolver.class})
    class SellHistoryTest {

        private final SellHistoryDao instance;

        public SellHistoryTest(SellHistoryDao instance) {
            this.instance = instance;
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsSellHistory")
        void insert_NewSell_notNull(SellHistoryEntity entity) {
            @Cleanup SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
            @Cleanup Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.persist(entity);
            assertThat(entity.getSellId()).isNotNull();
            session.getTransaction().commit();
        }

    }

    public static Stream<Arguments> argumentsSellHistory() {
        return Stream.of(Arguments.of(SellHistoryEntity.builder().sellDate(OffsetDateTime.now())
                                              .user(PersonalAccountEntity.builder().build())
                                              .itemId(ItemsEntity.builder().build()).quantity(2)
                                              .build()),
                         Arguments.of(SellHistoryEntity.builder()
                                              .sellDate(OffsetDateTime.now())
                                              .user(PersonalAccountEntity.builder().build())
                                              .itemId(ItemsEntity.builder().build())
                                              .quantity(3)
                                              .build()),
                         Arguments.of(SellHistoryEntity.builder()
                                              .sellDate(OffsetDateTime.now())
                                              .user(PersonalAccountEntity.builder().build())
                                              .itemId(ItemsEntity.builder().build())
                                              .quantity(10)
                                              .build()));
    }

    public static Stream<Arguments> argumentsPersonalAccount() {
        return Stream.of(Arguments.of(PersonalAccountEntity.builder().image("").name("Artem")
                                              .surname("Eranov").email("sobaka@mail.ru")
                                              .birthday(LocalDate.of(1990, 12, 12)).city("Oren")
                                              .address("Pushkina").country(Country.KAZAKHSTAN)
                                              .gender(Gender.MALE).phoneNumber("+79553330987")
                                              .password("1499")
                                              .build()),
                         Arguments.of(PersonalAccountEntity.builder().image("")
                                              .name("Danil").surname("Smirnov").email("ds_12@mail.ru")
                                              .birthday(LocalDate.of(2000, 3, 10)).city("Spb")
                                              .address("Lenina, b. 18").country(Country.RUSSIA)
                                              .gender(Gender.MALE).phoneNumber("+79553330987")
                                              .password("FNIM912KND")
                                              .build()),
                         Arguments.of(PersonalAccountEntity.builder().image("")
                                              .name("Dmitry").surname("Eranov").email("dmitry@mail.ru")
                                              .birthday(LocalDate.of(1997, 12, 20)).city("Minsk")
                                              .address("Pushkina").country(Country.BELARUS)
                                              .gender(Gender.MALE).phoneNumber("+79553330987")
                                              .password("Eranoff21").build())
        );

    public static Stream<Arguments> getArgumentsForItemsTestAndPersonalAccount() {
        return Stream.of(Arguments.of(ItemsEntity.builder()

                        .model("pixel a5")
                        .brand(Attributes.BrandEnum.GOOGLE)
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


    public static Stream<Arguments> argumentsForItemsTestAndPersonalAccount() {
        return Stream.of(Arguments.of(ItemsEntity.builder().model("pixel a5").brand(BrandEnum.Google)
                                              .attributes("128gb green").price(999.99).currency(CurrencyEnum.$)
                                              .quantity(57).build(), PersonalAccountEntity.builder().image("")
                                              .name("Artem").surname("Eranov").email("sobaka@mail.ru")
                                              .birthday(LocalDate.of(1990, 12, 12)).city("Oren").address("Pushkina")
                                              .country(Country.KAZAKHSTAN).gender(Gender.MALE)
                                              .phoneNumber("+79553330987").password("1499").build()));
    }
    public static Stream<Arguments> getArgumentsForItemsTest() {
        return Stream.of(Arguments.of(ItemsEntity.builder()
                .brand(Attributes.BrandEnum.GOOGLE)
                .model("pixel a5")
                .internalMemory(Attributes.InternalMemoryEnum.GB_16)
                .ram(Attributes.RamEnum.gb_4)
                .color("black")
                .os(ANDROID)
                .price(999.99)
                .currency(CurrencyEnum.$)
                .quantity(57)
                .build()));
    }

    public static Stream<Arguments> getListOfItemsOfArguments() {
        return Stream.of(Arguments.of(List.of(ItemsEntity.builder()
                        .brand(APPLE)
                        .model("iPhone 14")
                        .internalMemory(GB_512)
                        .ram(gb_8)
                        .color("space grey")
                        .os(IOS)
                        .price(119_990.00)
                        .currency(CurrencyEnum.₽)
                        .quantity(83)
                        .build(),
                ItemsEntity.builder()
                        .brand(APPLE)
                        .model("iPhone 11")
                        .internalMemory(GB_128)
                        .ram(gb_16)
                        .color("gold")
                        .os(IOS)
                        .price(79_999.99)
                        .currency(CurrencyEnum.₽)
                        .quantity(55)
                        .build(),
                ItemsEntity.builder()
                        .brand(APPLE)
                        .model("iPhone 15 Pro Max")
                        .internalMemory(GB_1024)
                        .ram(gb_16)
                        .color("black")
                        .os(IOS)
                        .price(215_999.99)
                        .currency(CurrencyEnum.₽)
                        .quantity(14)
                        .build(),
                ItemsEntity.builder()
                        .brand(APPLE)
                        .model("iPhone 14 Pro Max")
                        .internalMemory(GB_256)
                        .ram(gb_8)
                        .color("green")
                        .os(IOS)
                        .price(96_999.99)
                        .currency(CurrencyEnum.₽)
                        .quantity(99)
                        .build(),
                ItemsEntity.builder()
                        .brand(XIAOMI)
                        .model("Redmi A2+")
                        .internalMemory(GB_32)
                        .ram(gb_4)
                        .color("black")
                        .os(ANDROID)
                        .price(30_999.99)
                        .currency(CurrencyEnum.₽)
                        .quantity(114)
                        .build(),
                ItemsEntity.builder()
                        .brand(XIAOMI)
                        .model("13T")
                        .internalMemory(GB_64)
                        .ram(gb_3)
                        .color("black")
                        .os(ANDROID)
                        .price(8_999.99)
                        .currency(CurrencyEnum.₽)
                        .quantity(223)
                        .build(),
                ItemsEntity.builder()
                        .brand(SAMSUNG)
                        .model("Galaxy S21 FE")
                        .internalMemory(GB_128)
                        .ram(gb_12)
                        .color("yellow")
                        .os(ANDROID)
                        .price(28_999.99)
                        .currency(CurrencyEnum.₽)
                        .quantity(99)
                        .build(),
                ItemsEntity.builder()
                        .brand(SAMSUNG)
                        .model("Galaxy S23 Ultra")
                        .internalMemory(GB_1024)
                        .ram(gb_16)
                        .color("white")
                        .os(ANDROID)
                        .price(119_999.99)
                        .currency(CurrencyEnum.₽)
                        .quantity(8)
                        .build(),
                ItemsEntity.builder()
                        .brand(SAMSUNG)
                        .model("Galaxy A04")
                        .internalMemory(GB_32)
                        .ram(gb_2)
                        .color("brown")
                        .os(ANDROID)
                        .price(5_999.99)
                        .currency(CurrencyEnum.₽)
                        .quantity(99)
                        .build()
        )));
    }

    private static List<PersonalAccountEntity> getPersonalAccountEntities(int quantity) {
        List<PersonalAccountEntity> entities = List.of(PersonalAccountEntity.builder().image("").name("Artem")
                                                               .surname("Eranov").email("sobaka@mail.ru")
                                                               .birthday(LocalDate.of(1990, 12, 12)).city("Oren")
                                                               .address("Pushkina").country(Country.KAZAKHSTAN)
                                                               .gender(Gender.MALE).phoneNumber("+79553330987")
                                                               .password("1499")
                                                               .build(),
                                                       PersonalAccountEntity.builder().image("")
                                                               .name("Danil").surname("Smirnov").email("ds_12@mail.ru")
                                                               .birthday(LocalDate.of(2000, 3, 10)).city("Spb")
                                                               .address("Lenina, b. 18").country(Country.RUSSIA)
                                                               .gender(Gender.MALE).phoneNumber("+79553330987")
                                                               .password("FNIM912KND")
                                                               .build(),
                                                       PersonalAccountEntity.builder().image("")
                                                               .name("Dmitry").surname("Eranov").email("dmitry@mail.ru")
                                                               .birthday(LocalDate.of(1997, 12, 20)).city("Minsk")
                                                               .address("Pushkina").country(Country.BELARUS)
                                                               .gender(Gender.MALE).phoneNumber("+79553330987")
                                                               .password("Eranoff21").build());
        quantity = Math.min(quantity, entities.size());
        return entities.subList(0, quantity);
    }

    private static List<ItemsEntity> getItemsEntities(int quantity) {
        List<ItemsEntity> entities = List.of(ItemsEntity.builder().model("iPhone 14").brand(BrandEnum.Apple)
                                                     .attributes("128gb black").price(89_990.00)
                                                     .currency(CurrencyEnum.₽).quantity(83)
                                                     .build(),
                                             ItemsEntity.builder().model("iPhone 11")
                                                     .brand(BrandEnum.Apple).attributes("64gb red").price(79_999.99)
                                                     .currency(CurrencyEnum.₽).quantity(55)
                                                     .build(),
                                             ItemsEntity.builder().model("iPhone 15 Pro Max")
                                                     .brand(BrandEnum.Apple).attributes("1024gb white")
                                                     .price(215_999.99).currency(CurrencyEnum.₽).quantity(14)
                                                     .build(),
                                             ItemsEntity.builder().model("iPhone 14 Pro Max")
                                                     .brand(BrandEnum.Apple).attributes("512gb spaceGrey")
                                                     .price(36_999.99).currency(CurrencyEnum.₽).quantity(99)
                                                     .build(),
                                             ItemsEntity.builder().model("Redmi A2+")
                                                     .brand(BrandEnum.Xiaomi).attributes("128gb black").price(30_999.99)
                                                     .currency(CurrencyEnum.₽).quantity(114)
                                                     .build(),
                                             ItemsEntity.builder().model("13T")
                                                     .brand(BrandEnum.Xiaomi).attributes("64gb black").price(8_999.99)
                                                     .currency(CurrencyEnum.₽).quantity(223)
                                                     .build(),
                                             ItemsEntity.builder().model("Galaxy S21 FE")
                                                     .brand(BrandEnum.Samsung).attributes("128gb grey").price(28_999.99)
                                                     .currency(CurrencyEnum.₽).quantity(99)
                                                     .build(),
                                             ItemsEntity.builder().model("Galaxy S23 Ultra")
                                                     .brand(BrandEnum.Samsung).attributes("256gb white")
                                                     .price(119_999.99).currency(CurrencyEnum.₽).quantity(8)
                                                     .build(),
                                             ItemsEntity.builder().model("Galaxy A04")
                                                     .brand(BrandEnum.Samsung).attributes("8gb black").price(5_999.99)
                                                     .currency(CurrencyEnum.₽).quantity(99).build());
        quantity = Math.min(quantity, entities.size());
        return entities.subList(0, quantity);
    }

    private static List<SellHistoryEntity> getSellHistoryEntities(int quantity) {
        List<SellHistoryEntity> entities = List.of(SellHistoryEntity.builder().sellDate(OffsetDateTime.now())
                                                           .user(PersonalAccountEntity.builder().build())
                                                           .itemId(ItemsEntity.builder().build()).quantity(2)
                                                           .build(), SellHistoryEntity.builder()
                                                           .sellDate(OffsetDateTime.now())
                                                           .user(PersonalAccountEntity.builder().build())
                                                           .itemId(ItemsEntity.builder().build()).quantity(3)
                                                           .build(), SellHistoryEntity.builder()
                                                           .sellDate(OffsetDateTime.now())
                                                           .user(PersonalAccountEntity.builder().build())
                                                           .itemId(ItemsEntity.builder().build()).quantity(10).build());

        quantity = Math.min(quantity, entities.size());
        return entities.subList(0, quantity);
    }

    private static <T> void persistEntity(T entity, Session session) {
        session.beginTransaction();
        session.persist(entity);
        session.getTransaction().commit();
    }

    private static <T> void persistEntitiesList(List<T> list, Session session) {
        list.stream().map(element -> {
            session.beginTransaction();
            session.persist(element);
            session.getTransaction().commit();
            return element;
        }).collect(Collectors.toList());
    }
}
