package dao;

import dto.filter.AttributesFilter;
import entity.ItemSalesInformationEntity;
import entity.ItemsEntity;
import entity.PersonalAccountEntity;
import entity.SellHistoryEntity;
import entity.enums.Attributes;
import entity.enums.CountryEnum;
import entity.enums.CurrencyEnum;
import entity.enums.GenderEnum;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.EntityHandler;
import utlis.HibernateTestUtil;

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
    @MethodSource("argumentsWithOneItem")
    void insert_true(ItemsEntity itemEntity) {
        SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
        Session session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[]{Session.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));

        ItemsDao itemsDao = new ItemsDao(session);
        session.beginTransaction();
        ItemsEntity insertedItem = itemsDao.insert(itemEntity)
                .get();
        ItemsEntity actual = session.get(ItemsEntity.class, insertedItem.getId());
        session.getTransaction()
                .commit();
        assertThat(actual).isEqualTo(insertedItem);
    }

    @ParameterizedTest
    @MethodSource("argumentsWithOneItem")
    void update_true(ItemsEntity itemEntity) {
        SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
        Session session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[]{Session.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));

        ItemsDao itemsDao = new ItemsDao(session);
        session.beginTransaction();
        ItemsEntity updated = itemsDao.insert(itemEntity)
                .get();
        updated.setOs(IOS);
        itemsDao.update(updated);
        ItemsEntity actual = session.get(ItemsEntity.class, updated.getId());
        session.getTransaction()
                .commit();
        assertThat(actual).isEqualTo(updated);
    }

    @ParameterizedTest
    @MethodSource("argumentsWithOneItem")
    void remove_true(ItemsEntity itemEntity) {
        SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
        Session session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[]{Session.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));
        ItemsDao itemsDao = new ItemsDao(session);
        session.beginTransaction();
        ItemsEntity inserted = itemsDao.insert(itemEntity)
                .get();
        itemsDao.delete(inserted);
        ItemsEntity itemsEntityAfterDelete = session.get(ItemsEntity.class, inserted.getId());
        session.getTransaction()
                .commit();
        assertThat(itemsEntityAfterDelete).isNull();
    }

    @ParameterizedTest
    @MethodSource("argumentsWithOneItem")
    void getById_true(ItemsEntity itemEntity) {
        SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
        Session session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[]{Session.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));
        ItemsDao itemsDao = new ItemsDao(session);
        session.beginTransaction();
        ItemsEntity inserted = itemsDao.insert(itemEntity)
                .get();
        session.evict(inserted);
        ItemsEntity itemById = itemsDao.getById(1l)
                .get();
        session.getTransaction()
                .commit();
        assertThat(itemById).isEqualTo(itemEntity);
    }

    @Test
    void countItems_countIs3_True() {
        List<ItemsEntity> items = EntityHandler.getItemsEntities();
        SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
        Session session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[]{Session.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));
        ItemsDao itemsDao = new ItemsDao(session);
        persistEntitiesList(items, session);
        session.beginTransaction();
        long page = 2;
        long limit = 3;
        List<ItemsEntity> offsetLimitList = itemsDao.findItemsOnSpecificPage(page, limit);

        session.getTransaction()
                .commit();
        assertThat(offsetLimitList.size()).isEqualTo(3);
    }

    @Test
    void isFirstEntityOnThirdPage_isEqualExpectedEntity_True() {
        List<ItemsEntity> items = EntityHandler.getItemsEntities();
        SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
        Session session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[]{Session.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));
        ItemsDao itemsDao = new ItemsDao(session);
        persistEntitiesList(items, session);
        session.beginTransaction();
        long page = 3;
        long limit = 3;
        List<ItemsEntity> offsetLimitList = itemsDao.findItemsOnSpecificPage(page, limit);
        ItemsEntity expectedItemEntity = session.get(ItemsEntity.class, 7l);
        ItemsEntity actualItemEntity = offsetLimitList.get(0);
        assertThat(actualItemEntity).isEqualTo(expectedItemEntity);
        session.getTransaction()
                .commit();
    }

    @Test
    void isEntityGetsByUsingQuerydslFiltering_isEqualExpectedEntity_True() {
        List<ItemsEntity> items = EntityHandler.getItemsEntities();
        SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
        Session session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[]{Session.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));
        ItemsDao itemsDao = new ItemsDao(session);
        persistEntitiesList(items, session);
        AttributesFilter filter = AttributesFilter.builder()
                .brand(APPLE)
                .os(IOS)
                .internalMemory(GB_512)
                .ram(gb_8)
                .build();
        ItemsEntity expected = ItemsEntity.builder()
                .brand(APPLE)
                .model("iPhone 14")
                .internalMemory(GB_512)
                .ram(gb_8)
                .color("space grey")
                .os(IOS)
                .itemSalesInformation(ItemSalesInformationEntity.builder()
                        .price(119_990.00)
                        .currency(CurrencyEnum.₽)
                        .quantity(83)
                        .build())
                .build();
        session.beginTransaction();
        session.clear();
        long page = 1;
        long limit = 3;
        List<ItemsEntity> itemsWithFiltering = itemsDao.findItemsWithParameters(filter, page, limit);
        session.getTransaction()
                .commit();
        assertThat(itemsWithFiltering.get(0)).isEqualTo(expected);
    }


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

    public static Stream<Arguments> argumentsWithOneItem() {
        return Stream.of(Arguments.of(ItemsEntity.builder()
                .brand(GOOGLE)
                .model("pixel a5")
                .internalMemory(Attributes.InternalMemoryEnum.GB_16)
                .ram(gb_4)
                .color("yellow")
                .os(ANDROID)
                .image(new byte[0])
                .itemSalesInformation(ItemSalesInformationEntity.builder()
                        .price(999.99)
                        .currency(CurrencyEnum.$)
                        .quantity(57)
                        .build())
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
                        .itemSalesInformation(ItemSalesInformationEntity.builder()
                                .price(119_990.00)
                                .currency(CurrencyEnum.₽)
                                .quantity(83)
                                .build())
                        .build(),
                ItemsEntity.builder()
                        .brand(APPLE)
                        .model("iPhone 11")
                        .internalMemory(GB_128)
                        .ram(gb_16)
                        .color("gold")
                        .os(IOS)
                        .itemSalesInformation(ItemSalesInformationEntity.builder()
                                .price(79_999.99)
                                .currency(CurrencyEnum.₽)
                                .quantity(55)
                                .build())
                        .build(),
                ItemsEntity.builder()
                        .brand(APPLE)
                        .model("iPhone 15 Pro Max")
                        .internalMemory(GB_1024)
                        .ram(gb_16)
                        .color("black")
                        .os(IOS)
                        .itemSalesInformation(ItemSalesInformationEntity.builder()
                                .price(215_999.99)
                                .currency(CurrencyEnum.₽)
                                .quantity(14)
                                .build())
                        .build(),
                ItemsEntity.builder()
                        .brand(APPLE)
                        .model("iPhone 14 Pro Max")
                        .internalMemory(GB_256)
                        .ram(gb_8)
                        .color("green")
                        .os(IOS)
                        .itemSalesInformation(ItemSalesInformationEntity.builder()
                                .price(96_999.99)
                                .currency(CurrencyEnum.₽)
                                .quantity(99)
                                .build())
                        .build(),
                ItemsEntity.builder()
                        .brand(XIAOMI)
                        .model("Redmi A2+")
                        .internalMemory(GB_32)
                        .ram(gb_4)
                        .color("black")
                        .os(ANDROID)
                        .itemSalesInformation(ItemSalesInformationEntity.builder()
                                .price(30_999.99)
                                .currency(CurrencyEnum.₽)
                                .quantity(114)
                                .build())
                        .build(),
                ItemsEntity.builder()
                        .brand(XIAOMI)
                        .model("13T")
                        .internalMemory(GB_64)
                        .ram(gb_3)
                        .color("black")
                        .os(ANDROID)
                        .itemSalesInformation(ItemSalesInformationEntity.builder()
                                .price(8_999.99)
                                .currency(CurrencyEnum.₽)
                                .quantity(223)
                                .build())
                        .build(),
                ItemsEntity.builder()
                        .brand(SAMSUNG)
                        .model("Galaxy S21 FE")
                        .internalMemory(GB_128)
                        .ram(gb_12)
                        .color("yellow")
                        .os(ANDROID)
                        .itemSalesInformation(ItemSalesInformationEntity.builder()
                                .price(28_999.99)
                                .currency(CurrencyEnum.₽)
                                .quantity(99)
                                .build())
                        .build(),
                ItemsEntity.builder()
                        .brand(SAMSUNG)
                        .model("Galaxy S23 Ultra")
                        .internalMemory(GB_1024)
                        .ram(gb_16)
                        .color("white")
                        .os(ANDROID)
                        .itemSalesInformation(ItemSalesInformationEntity.builder()
                                .price(119_999.99)
                                .currency(CurrencyEnum.₽)
                                .quantity(8)
                                .build())
                        .build(),
                ItemsEntity.builder()
                        .brand(SAMSUNG)
                        .model("Galaxy A04")
                        .internalMemory(GB_32)
                        .ram(gb_2)
                        .color("brown")
                        .os(ANDROID)
                        .itemSalesInformation(ItemSalesInformationEntity.builder()
                                .price(5_999.99)
                                .currency(CurrencyEnum.₽)
                                .quantity(99)
                                .build())
                        .build())));
    }
}
