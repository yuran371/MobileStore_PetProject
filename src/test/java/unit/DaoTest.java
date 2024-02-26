package unit;

import com.querydsl.core.Tuple;
import dao.PersonalAccountDao;
import dao.SellHistoryDao;
import entity.ItemsEntity;
import entity.PersonalAccountEntity;
import entity.PremiumUserEntity;
import entity.SellHistoryEntity;
import entity.enums.*;
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

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static entity.enums.Attributes.BrandEnum.*;
import static entity.enums.Attributes.InternalMemoryEnum.*;
import static entity.enums.Attributes.OperatingSystemEnum.ANDROID;
import static entity.enums.Attributes.OperatingSystemEnum.IOS;
import static entity.enums.Attributes.RamEnum.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;
import static util.EntityHandler.dropEntities;
import static util.EntityHandler.dropEntity;


@Slf4j
public class DaoTest {
    @Nested
    @TestInstance(PER_METHOD)
    @Tag(value = "PersonalAccountDao")
    @ExtendWith(value = {PersonalAccountParameterResolver.class})
    class PersonalAccount {

        private PersonalAccountDao personalAccountDao;
        private static SessionFactory entityManager = HibernateTestUtil.getSessionFactory();

        public PersonalAccount(PersonalAccountDao instance) {
            this.personalAccountDao = instance;
        }

        @AfterAll
        public static void closeTestSessionFactory() {
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
//            List<SellHistoryEntity> sellHistoryEntityList = getSellHistoryEntities(3);
//            @Cleanup SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
//            @Cleanup Session session = sessionFactory.openSession();
//            session.beginTransaction();
//            session.persist(account); Long accountId = account.getId();
//            sellHistoryEntityList.stream()
//                    .forEach(sellHistoryEntity -> {
//                sellHistoryEntity.setUser(account);
//                session.persist(sellHistoryEntity);
//            }); session.detach(account);
//            session.getTransaction().commit();
//            session.beginTransaction();
//            PersonalAccountEntity personalAccountEntity = session.get(PersonalAccountEntity.class, accountId);
//            assertThat(personalAccountEntity.getOrders().size()).isEqualTo(sellHistoryEntityList.size());
//            sellHistoryEntityList.stream().forEach(sellHistoryEntity -> session.remove(sellHistoryEntity));
//            session.remove(personalAccountEntity); session.getTransaction().commit();
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsPersonalAccount")
        void validateAuth_validUser_returnUser(PersonalAccountEntity account) {
            @Cleanup Session session = entityManager.openSession();
            persistEntity(account, session);
            Optional<PersonalAccountEntity> personalAccountEntity = personalAccountDao
                    .validateAuth(account.getEmail(), account.getPassword(), session);
            assertThat(personalAccountEntity.get()).isNotNull();
            assertThat(personalAccountEntity.get().getEmail()).isEqualTo(account.getEmail());
            assertThat(personalAccountEntity.get().getPassword()).isEqualTo(account.getPassword());
            dropEntity(account, session);
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsPersonalAccount")
        void getByEmail_validUser_returnUser(PersonalAccountEntity account) {
            @Cleanup Session session = entityManager.openSession();
            persistEntity(account, session);
            Optional<PersonalAccountEntity> personalAccountEntity = personalAccountDao
                    .getByEmail(account.getEmail(), session);
            assertThat(personalAccountEntity.get()).isNotNull();
            assertThat(personalAccountEntity.get().getId()).isEqualTo(account.getId());
            assertThat(personalAccountEntity.get().getEmail()).isEqualTo(account.getEmail());
            assertThat(personalAccountEntity.get().getPassword()).isEqualTo(account.getPassword());
            dropEntity(account, session);
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsPersonalAccount")
        void checkDiscount_premiumUser_returnDiscount(PersonalAccountEntity account) {
            @Cleanup Session session = entityManager.openSession();
            PremiumUserEntity premiumUserEntity = new PremiumUserEntity(account, DiscountEnum.FIVE_PERCENT);
            persistEntity(premiumUserEntity, session);
//            Optional<DiscountEnum> discount = personalAccountDao.checkDiscount(premiumUserEntity.getId(), session);
//            assertThat(discount.get()).isEqualTo(DiscountEnum.FIVE_PERCENT);
            dropEntity(premiumUserEntity, session);
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsPersonalAccount")
        void checkDiscount_notPremiumUser_returnNull(PersonalAccountEntity account) {
            @Cleanup Session session = entityManager.openSession();
            persistEntity(account, session);
//            Optional<DiscountEnum> discount = personalAccountDao.checkDiscount(account.getId(), session);
//            assertThat(discount).isEmpty();
            dropEntity(account, session);
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsPersonalAccount")
        void getAllBoughtPhones_havePhones_returnList(PersonalAccountEntity account) {
            List<SellHistoryEntity> sellHistoryEntities = getSellHistoryEntities(3);
            List<ItemsEntity> itemsEntities = getItemsEntities(3);
            @Cleanup Session session = entityManager.openSession();
            persistEntity(account, session);
            persistEntitiesList(itemsEntities, session);
            for (int i = 0; i < sellHistoryEntities.size(); i++) {
                SellHistoryEntity sellHistoryEntity = sellHistoryEntities.get(i);
                sellHistoryEntity.setUser(account);
                sellHistoryEntity.setItemId(itemsEntities.get(i));
            }
            persistEntitiesList(sellHistoryEntities, session);
            List<ItemsEntity> allBoughtPhones = personalAccountDao.getAllBoughtPhones(account.getId(), session);
            assertThat(allBoughtPhones.size()).isEqualTo(itemsEntities.size());
            assertThat(allBoughtPhones).extracting("id").contains(itemsEntities.get(0).getId(),
                                                                  itemsEntities.get(1).getId(),
                                                                  itemsEntities.get(2).getId());
            dropEntities(session, sellHistoryEntities, itemsEntities);
            dropEntity(account, session);
        }

        @Tag("Unit")
        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsPersonalAccount")
        void getAllBoughtPhones_noPhones_returnEmptyList(PersonalAccountEntity account) {
            @Cleanup Session session = entityManager.openSession();
            persistEntity(account, session);
            List<ItemsEntity> allBoughtPhones = personalAccountDao.getAllBoughtPhones(account.getId(), session);
            assertThat(allBoughtPhones).isEmpty();
            dropEntity(account, session);
        }

        @Test
        void getTopTenMostSpenders_haveUsers_returnTop() {
            List<PersonalAccountEntity> accounts = getPersonalAccountEntities(3);
            List<ItemsEntity> items = getItemsEntities(10);
            List<SellHistoryEntity> sellHistoryEntities = getSellHistoryEntities(10);
            @Cleanup Session session = entityManager.openSession();
            persistEntitiesList(accounts, session);
            persistEntitiesList(items, session);
            for (SellHistoryEntity entity : sellHistoryEntities) {
                Random random = new Random();
                entity.setUser(accounts.get(random.nextInt(0, accounts.size())));
                entity.setItemId(items.get(random.nextInt(0, items.size())));
            }
            persistEntitiesList(sellHistoryEntities, session);
            List<Tuple> topTenMostSpenders = personalAccountDao.getTopTenMostSpenders(session);
            for (int i = 1; i < topTenMostSpenders.size(); i++) {
                Double spender = (Double) topTenMostSpenders.get(i - 1).get(1, Double.class);
                Double nextSpender = (Double) topTenMostSpenders.get(i).get(1, Double.class);
                assertThat(spender).isGreaterThan(nextSpender);
            }
            dropEntities(session, accounts, items, sellHistoryEntities);
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
            assertThat(entity.getId()).isNotNull();
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

    private static List<PersonalAccountEntity> getPersonalAccountEntities(int quantity) {
        List<PersonalAccountEntity> entities = List.of(PersonalAccountEntity.builder()
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
                        .build(),
                PersonalAccountEntity.builder()
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
                        .build(),
                PersonalAccountEntity.builder()
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
                        .build());
        quantity = Math.min(quantity, entities.size());
        return entities.subList(0, quantity);
    }

    private static List<ItemsEntity> getItemsEntities(int quantity) {
        List<ItemsEntity> entities = List.of(ItemsEntity.builder()
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
        );
        quantity = Math.min(quantity, entities.size());
        return entities.subList(0, quantity);
    }

    private static List<SellHistoryEntity> getSellHistoryEntities(int quantity) {
        List<SellHistoryEntity> entities = List.of(SellHistoryEntity.builder()
                .sellDate(OffsetDateTime.now())
                .user(PersonalAccountEntity.builder()
                        .build())
                .itemId(ItemsEntity.builder()
                        .build())
                .quantity(2)
                .build(), SellHistoryEntity.builder()
                .sellDate(OffsetDateTime.now())
                .user(PersonalAccountEntity.builder()
                        .build())
                .itemId(ItemsEntity.builder()
                        .build())
                .quantity(3)
                .build(), SellHistoryEntity.builder()
                .sellDate(OffsetDateTime.now())
                .user(PersonalAccountEntity.builder()
                        .build())
                .itemId(ItemsEntity.builder()
                        .build())
                .quantity(10)
                .build());

        quantity = Math.min(quantity, entities.size());
        return entities.subList(0, quantity);
    }

    private static <T> void persistEntity(T entity, Session session) {
        session.beginTransaction();
        session.persist(entity);
        session.getTransaction().commit();
    }

    private static <T> void persistEntitiesList(List<T> list, Session session) {
        list.stream()
                .map(element -> {
                    session.beginTransaction();
                    session.persist(element);
                    session.getTransaction()
                            .commit();
                    return element;
                })
                .collect(Collectors.toList());
    }


}
