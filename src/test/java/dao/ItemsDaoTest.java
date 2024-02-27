package dao;

import entity.CurrencyInfo;
import entity.ItemsEntity;
import entity.PersonalAccountEntity;
import entity.SellHistoryEntity;
import entity.enums.Attributes;
import entity.enums.CountryEnum;
import entity.enums.CurrencyEnum;
import entity.enums.GenderEnum;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.EntityHandler;
import util.HibernateTestUtil;

import java.lang.reflect.Proxy;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

import static entity.enums.Attributes.BrandEnum.*;
import static entity.enums.Attributes.InternalMemoryEnum.*;
import static entity.enums.Attributes.OperatingSystemEnum.ANDROID;
import static entity.enums.Attributes.OperatingSystemEnum.IOS;
import static entity.enums.Attributes.RamEnum.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;
import static util.EntityHandler.persistEntitiesList;

@TestInstance(PER_METHOD)
@Tag(value = "ItemsDaoTest")
public class ItemsDaoTest {

    @ParameterizedTest
    @MethodSource("unit.DaoTest#argumentsListOfItems")
    void countItems_countIs3_True(List<ItemsEntity> list) {
        @Cleanup SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        ItemsDao itemsDao = new ItemsDao(session);
        session.beginTransaction();
        for (ItemsEntity item : list) {
            session.persist(item);
        }
        long page = 2;
        List<ItemsEntity> offsetLimitList = itemsDao.findItemsOnSpecificPage(page, session);

        session.getTransaction()
                .commit();
        assertThat(offsetLimitList.size()).isEqualTo(3);
    }

    @Test
    void testEntityManagerAndGetCurrentSessionMethod() {
        List<ItemsEntity> items = EntityHandler.getItemsEntities();
        List<PersonalAccountEntity> accounts = EntityHandler.getPersonalAccountEntities();
        List<SellHistoryEntity> sellHistories = EntityHandler.getSellHistoryEntities();
        SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
        Session session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[]{Session.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));

        session.beginTransaction();
        ItemsDao itemsDao = new ItemsDao(session);
        persistEntitiesList(accounts, session);
        persistEntitiesList(items, session);
        for (int i = 0; i < 3; i++) {
            items.get(i)
                    .addPhoneOrder(sellHistories.get(i));
            accounts.get(i)
                    .addPurchase(sellHistories.get(i));
        }
        persistEntitiesList(sellHistories, session);
        System.out.println("------");
        itemsDao.getById(1l)
                .ifPresentOrElse(System.out::println, () -> System.out.println("125555"));
        List<ItemsEntity> list = itemsDao.findAllViaQuerydsl();
        for (ItemsEntity l : list) {
            System.out.println(l);
        }
        session.getTransaction()
                .commit();
    }

//    @ParameterizedTest
//    @MethodSource("unit.DaoTest#argumentsListOfItems")
//    void isFirstEntityOnThirdPage_isEqualExpectedEntity_True(List<ItemsEntity> list) {
//        @Cleanup Session session = HibernateTestUtil.getSessionFactory()
//                .openSession();
//        session.beginTransaction();
//        for (ItemsEntity item : list) {
//            session.persist(item);
//        }
//        long page = 3;
//        List<ItemsEntity> offsetLimitList = itemsDao.findItemsOnSpecificPage(page, session);
//        ItemsEntity firstItemOnThirdPage = offsetLimitList.get(0);
//        session.getTransaction()
//                .commit();
//        ItemsEntity expectedEntity = list.get(6);
//        assertThat(firstItemOnThirdPage).isEqualTo(expectedEntity);
//    }

//    @ParameterizedTest
//    @MethodSource("unit.DaoTest#argumentsListOfItems")
//    void isEntityGetsByUsingQuerydslFiltering_isEqualExpectedEntity_True(List<ItemsEntity> list) {
//        @Cleanup Session session = HibernateSessionFactory.getSessionFactory()
//                .openSession();
//        AttributesFilter filter = AttributesFilter.builder()
//                .brand(APPLE)
//                .os(IOS)
//                .internalMemory(GB_512)
//                .ram(gb_8)
//                .build();
//        ItemsEntity expected = ItemsEntity.builder()
//                .brand(APPLE)
//                .model("iPhone 14")
//                .internalMemory(GB_512)
//                .ram(gb_8)
//                .color("space grey")
//                .os(IOS)
//                .price(119_990.00)
//                .currency(CurrencyEnum.₽)
//                .quantity(83)
//                .build();
//        persistEntitiesList(list, session);
//        session.beginTransaction();
//        session.clear();
//        List<ItemsEntity> items = itemsDao.findItemsWithParameters(filter, session);
//        session.getTransaction()
//                .commit();
//        assertThat(items.get(0)).isEqualTo(expected);
//    }

    @ParameterizedTest
    @MethodSource("dao.ItemsDaoTest.argumentsListOfItems")
    void currencyInfo(List<ItemsEntity> itemsEntity) {
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

//    @Test
//    void testGraphs() {
//        List<ItemsEntity> items = EntityHandler.getItemsEntities();     // ItemsEntity has one-to-many mapping
//        List<PersonalAccountEntity> accounts = EntityHandler.getPersonalAccountEntities();  // has one-to-many mapping
//        List<SellHistoryEntity> sellHistory = EntityHandler.getSellHistoryEntities();       // has many-to-one mapping
//        @Cleanup SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
//        @Cleanup Session session = sessionFactory.openSession();
//        persistEntitiesList(accounts, session);     // fills the db with entities of items
//        persistEntitiesList(items, session);        // fills the db with entities of accounts
//        for (int i = 0; i < 3; i++) {
//            items.get(i)
//                    .addPhoneOrder(sellHistory.get(i));
//            accounts.get(i)
//                    .addPurchase(sellHistory.get(i));
//        }
//        persistEntitiesList(sellHistory, session);  // fills the db with entities of sellHistory
//        session.beginTransaction();
//        session.clear();    // need to clean of 1st lvl cache
////        Map<String, Object> properties = Map.of(
////                GraphSemantic.LOAD.getJakartaHintName(), session.getEntityGraph("withSellHistoryGraph")
////
////        );
//        Statistics statistics = sessionFactory.getStatistics();
//        statistics.setStatisticsEnabled(true);
//        ItemsEntity item = new JPAQuery<ItemsEntity>(session).select(itemsEntity)
//                .from(itemsEntity)
//                .where(itemsEntity.id.eq(1l))
//                .setHint(GraphSemantic.LOAD.getJakartaHintName(), session.getEntityGraph("withSellHistoryGraph"))
//                .fetchOne();
////        ItemsEntity itemsEntity = session.find(ItemsEntity.class, 1l, properties);
//        item.getPhoneOrders()
//                .get(0)
//                .getUser()
//                .getEmail();   // Here compiling required Sql query
//        session.getTransaction()
//                .commit();
//        String[] queries = statistics.getQueries();
//        System.out.println("1: " + Arrays.toString(queries));
//        System.out.println("2: " + statistics.getCloseStatementCount());
//        System.out.println("3: " + statistics.getEntityLoadCount());
//        System.out.println("4: " + statistics.getConnectCount());
////        System.out.println("5: " + statistics.getEntityStatistics("QItemsEntity"));
//        System.out.println("6: " + statistics.getQueryStatistics("insert"));
//        System.out.println("7: " + statistics.getSlowQueries());
//        System.out.println("8: " + statistics.getTransactionCount());
//        statistics.logSummary();
//        boolean checkForJoin = Arrays.stream(queries)
//                .anyMatch(s -> s.contains("join"));
//    }

    public static Stream<Arguments> argumentsSellHistory() {
        return Stream.of(Arguments.of(SellHistoryEntity.builder()
                        .sellDate(OffsetDateTime.now())
                        .user(PersonalAccountEntity.builder()
                                .build())
                        .itemId(ItemsEntity.builder()
                                .build())
                        .quantity(2)
                        .build()),
                Arguments.of(SellHistoryEntity.builder()
                        .sellDate(OffsetDateTime.now())
                        .user(PersonalAccountEntity.builder()
                                .build())
                        .itemId(ItemsEntity.builder()
                                .build())
                        .quantity(3)
                        .build()),
                Arguments.of(SellHistoryEntity.builder()
                        .sellDate(OffsetDateTime.now())
                        .user(PersonalAccountEntity.builder()
                                .build())
                        .itemId(ItemsEntity.builder()
                                .build())
                        .quantity(10)
                        .build()));
    }

    public static Stream<Arguments> argumentsPersonalAccount() {
        return Stream.of(Arguments.of(PersonalAccountEntity.builder()
                        .image("")
                        .name("Artem")
                        .surname("Eranov")
                        .email("sobaka@mail.ru")
                        .birthday(LocalDate.of(1990, 12, 12))
                        .city("Oren")
                        .address("Pushkina")
                        .countryEnum(CountryEnum.KAZAKHSTAN)
                        .genderEnum(GenderEnum.MALE)
                        .phoneNumber("+79553330987")
                        .password("1499")
                        .build()),
                Arguments.of(PersonalAccountEntity.builder()
                        .image("")
                        .name("Danil")
                        .surname("Smirnov")
                        .email("ds_12@mail.ru")
                        .birthday(LocalDate.of(2000, 3, 10))
                        .city("Spb")
                        .address("Lenina, b. 18")
                        .countryEnum(CountryEnum.RUSSIA)
                        .genderEnum(GenderEnum.MALE)
                        .phoneNumber("+79553330987")
                        .password("FNIM912KND")
                        .build()),
                Arguments.of(PersonalAccountEntity.builder()
                        .image("")
                        .name("Dmitry")
                        .surname("Eranov")
                        .email("dmitry@mail.ru")
                        .birthday(LocalDate.of(1997, 12, 20))
                        .city("Minsk")
                        .address("Pushkina")
                        .countryEnum(CountryEnum.BELARUS)
                        .genderEnum(GenderEnum.MALE)
                        .phoneNumber("+79553330987")
                        .password("Eranoff21")
                        .build())
        );

    }

    public static Stream<Arguments> argumentsForItemsTest() {
        return Stream.of(Arguments.of(ItemsEntity.builder()
                .brand(GOOGLE)
                .model("pixel a5")
                .internalMemory(Attributes.InternalMemoryEnum.GB_16)
                .ram(gb_4)
                .color("black")
                .os(ANDROID)
                .price(999.99)
                .currency(CurrencyEnum.$)
                .quantity(57)
                .build()));
    }

    public static Stream<Arguments> argumentsListOfItems() {
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
}
