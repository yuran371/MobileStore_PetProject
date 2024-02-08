package unit;

import dao.ItemsDao;
import dao.SellHistoryDao;
import dto.AttributesFilter;
import entity.CurrencyInfo;
import entity.ItemsEntity;
import entity.PersonalAccountEntity;
import entity.SellHistoryEntity;
import entity.enums.Attributes;
import entity.enums.CurrencyEnum;
import extentions.SellHistoryParameterResolver;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.EntityHandler;
import util.HibernateTestUtil;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

import static entity.enums.Attributes.BrandEnum.*;
import static entity.enums.Attributes.InternalMemoryEnum.*;
import static entity.enums.Attributes.OperatingSystemEnum.ANDROID;
import static entity.enums.Attributes.OperatingSystemEnum.IOS;
import static entity.enums.Attributes.RamEnum.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;


@Slf4j
public class DaoTest {
    @Nested
    @Tag(value = "ItemsDao")
    class Items {
        ItemsDao itemsDao = ItemsDao.getInstance();

        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsListOfItems")
        void countItems_countIs3_True(List<ItemsEntity> list) {
            @Cleanup Session session = HibernateTestUtil.getSessionFactory().openSession();
            session.beginTransaction();
            for (ItemsEntity item : list) {
                session.persist(item);
            }
            long page = 2;
            List<ItemsEntity> offsetLimitList = itemsDao.findItemsOnSpecificPage(page, session);

            session.getTransaction().commit();
            assertThat(offsetLimitList.size()).isEqualTo(3);
            // Написать тест: смещение списка и сравнение полученных items
        }

        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsListOfItems")
        void isFirstEntityOnThirdPage_isEqualExpectedEntity_True(List<ItemsEntity> list) {
            @Cleanup Session session = HibernateTestUtil.getSessionFactory().openSession();
            session.beginTransaction();
            for (ItemsEntity item : list) {
                session.persist(item);
            }
            long page = 3;
            List<ItemsEntity> offsetLimitList = itemsDao.findItemsOnSpecificPage(page, session);
            ItemsEntity firstItemOnThirdPage = offsetLimitList.get(0);
            session.getTransaction().commit();
            ItemsEntity expectedEntity = list.get(6);
            assertThat(firstItemOnThirdPage).isEqualTo(expectedEntity);
        }

        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsListOfItems")
        void method1(List<ItemsEntity> list) {
            @Cleanup Session session = HibernateTestUtil.getSessionFactory().openSession();
            AttributesFilter filter = AttributesFilter.builder().brand(APPLE).os(IOS).internalMemory(GB_1024).ram(gb_16)
                    .build();
            EntityHandler.persistEntitiesList(list, session);
            session.beginTransaction();
            List<ItemsEntity> items = itemsDao.findItemsWithParameters(filter, session);
            session.getTransaction().commit();
            for (ItemsEntity item : items) {
                System.out.println(item);
            }
        }

        @ParameterizedTest
        @MethodSource("unit.DaoTest#argumentsForItemsTest")
        void currencyInfo(ItemsEntity itemsEntity) {
            @Cleanup Session session = HibernateTestUtil.getSessionFactory().openSession();
            session.beginTransaction();
            session.persist(itemsEntity);
            ItemsEntity item = session.get(ItemsEntity.class, 1l);
            item.getCurrencyInfos().add(CurrencyInfo.of(1000.00, CurrencyEnum.$));
            item.getCurrencyInfos().add(CurrencyInfo.of(89_000.00, CurrencyEnum.₽));
            System.out.println(item.getCurrencyInfos());
            session.getTransaction().commit();
        }

//        @ParameterizedTest
//        @DisplayName("если orphanRemoval=true, то при удалении комментария из топика он удаляется из базы")
//        @MethodSource("unit.DaoTest#argumentsForItemsTestAndPersonalAccount")
//        void givenOrphanRemovalTrue_whenRemoveSellHistoryEntityFromPhoneOrders_thenItRemovedFromDatabase
//        (ItemsEntity itemsEntity, PersonalAccountEntity personalAccountEntity) {
//            @Cleanup Session session = HibernateTestUtil.getSessionFactory()
//                    .openSession();
//            session.beginTransaction();
//            itemsDao.insertViaHibernate(itemsEntity, session);
//            session.persist(personalAccountEntity);
//            Long itemId = itemsEntity.getId();
////            List<SellHistoryEntity> collectOfSellHistoryEntity =
////                //    getArgumentForSellHistory().map(arguments -> (SellHistoryEntity) arguments.get()[0])
////                //            .collect(Collectors.toList());
////            collectOfSellHistoryEntity.stream()
////                    .forEach(sellHistoryEntity -> {
////                        itemsEntity.addPhoneOrder(sellHistoryEntity);
////                        personalAccountEntity.addPurchase(sellHistoryEntity);
////                        session.persist(sellHistoryEntity);
////                    });
//            /*
//             * 64-68стр Нужны были для "обновления" данных (актуализации List). После добавления 57стр deprecated
//             * */
////            session.getTransaction()
////                    .commit();
////            session.detach(itemsEntity);
////            session.beginTransaction();
////            ItemsEntity itemsEntity1 = session.get(itemsEntity.getClass(), itemId);
////            itemsEntity.getPhoneOrders()
////                    .remove(0);
////            itemsEntity.removePhoneOrder(itemsEntity.getPhoneOrders()
////                    .get(0));   //  Тестируем удаление sellHistoryEntity с orphanRemoval = true
////            personalAccountEntity.removePurchase(personalAccountEntity.getPhonePurchases()
////                    .get(0));   //  Тестируем удаление sellHistoryEntity с orphanRemoval = true
////
////            SellHistoryEntity sellHistoryEntity = collectOfSellHistoryEntity.get(0);
////            Long sellId = sellHistoryEntity
////                    .getSellId();
////            session.remove(sellHistoryEntity);
////            SellHistoryEntity sellHistoryEntityIsNull = session.get(sellHistoryEntity.getClass(), sellId);  //
////            // sellHistoryEntity должен быть null после session.remove(sellHistoryEntity);
////            session.remove(itemsEntity);
////            session.remove(personalAccountEntity);
////            session.flush();
////            session.getTransaction()
////                    .commit();
////            assertThat(sellHistoryEntityIsNull).isNull();   //  Проверка, удалился ли SellHistory из таблицы
//
//            log.info("Just added: {} {} {} {} qt: {}", itemsEntity.getBrand(), itemsEntity.getModel(),
//                    itemsEntity.getPrice(), itemsEntity.getCurrency(), itemsEntity.getQuantity());
//        }
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

    public static Stream<SellHistoryEntity> argumentsSellHistory() {
        return Stream.of(SellHistoryEntity.builder().sellDate(OffsetDateTime.now())
                                 .user(PersonalAccountEntity.builder().build())
                                 .itemId(ItemsEntity.builder().build()).quantity(2)
                                 .build(),
                         SellHistoryEntity.builder()
                                 .sellDate(OffsetDateTime.now())
                                 .user(PersonalAccountEntity.builder().build())
                                 .itemId(ItemsEntity.builder().build())
                                 .quantity(3)
                                 .build(),
                         SellHistoryEntity.builder()
                                 .sellDate(OffsetDateTime.now())
                                 .user(PersonalAccountEntity.builder().build())
                                 .itemId(ItemsEntity.builder().build())
                                 .quantity(10)
                                 .build());
    }


    public static Stream<Arguments> argumentsForItemsTest() {
        return Stream.of(Arguments.of(ItemsEntity.builder().brand(GOOGLE).model("pixel a5")
                                              .internalMemory(Attributes.InternalMemoryEnum.GB_16).ram(gb_4)
                                              .color("black").os(ANDROID).price(999.99).currency(CurrencyEnum.$)
                                              .quantity(57).build()));
    }

    public static Stream<Arguments> argumentsListOfItems() {
        return Stream.of(Arguments.of(List.of(ItemsEntity.builder().brand(APPLE).model("iPhone 14")
                                                      .internalMemory(GB_512).ram(gb_8).color("space grey").os(IOS)
                                                      .price(119_990.00).currency(CurrencyEnum.₽).quantity(83)
                                                      .build(), ItemsEntity.builder().brand(APPLE).model("iPhone 11")
                                                      .internalMemory(GB_128).ram(gb_16).color("gold").os(IOS)
                                                      .price(79_999.99).currency(CurrencyEnum.₽).quantity(55)
                                                      .build(), ItemsEntity.builder().brand(APPLE)
                                                      .model("iPhone 15 Pro Max").internalMemory(GB_1024).ram(gb_16)
                                                      .color("black").os(IOS).price(215_999.99).currency(CurrencyEnum.₽)
                                                      .quantity(14).build(), ItemsEntity.builder().brand(APPLE)
                                                      .model("iPhone 14 Pro Max").internalMemory(GB_256).ram(gb_8)
                                                      .color("green").os(IOS).price(96_999.99).currency(CurrencyEnum.₽)
                                                      .quantity(99).build(), ItemsEntity.builder().brand(XIAOMI)
                                                      .model("Redmi A2+").internalMemory(GB_32).ram(gb_4).color("black")
                                                      .os(ANDROID).price(30_999.99).currency(CurrencyEnum.₽)
                                                      .quantity(114).build(), ItemsEntity.builder().brand(XIAOMI)
                                                      .model("13T").internalMemory(GB_64).ram(gb_3).color("black")
                                                      .os(ANDROID).price(8_999.99).currency(CurrencyEnum.₽)
                                                      .quantity(223).build(), ItemsEntity.builder().brand(SAMSUNG)
                                                      .model("Galaxy S21 FE").internalMemory(GB_128).ram(gb_12)
                                                      .color("yellow").os(ANDROID).price(28_999.99)
                                                      .currency(CurrencyEnum.₽).quantity(99)
                                                      .build(), ItemsEntity.builder().brand(SAMSUNG)
                                                      .model("Galaxy S23 Ultra").internalMemory(GB_1024).ram(gb_16)
                                                      .color("white").os(ANDROID).price(119_999.99)
                                                      .currency(CurrencyEnum.₽).quantity(8)
                                                      .build(), ItemsEntity.builder().brand(SAMSUNG).model("Galaxy A04")
                                                      .internalMemory(GB_32).ram(gb_2).color("brown").os(ANDROID)
                                                      .price(5_999.99).currency(CurrencyEnum.₽).quantity(99).build())));
    }


}
