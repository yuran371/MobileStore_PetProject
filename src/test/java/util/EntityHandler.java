package util;

import entity.*;
import entity.enums.Attributes;
import entity.enums.CountryEnum;
import entity.enums.CurrencyEnum;
import entity.enums.GenderEnum;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static entity.enums.Attributes.BrandEnum.*;
import static entity.enums.Attributes.InternalMemoryEnum.*;
import static entity.enums.Attributes.OperatingSystemEnum.ANDROID;
import static entity.enums.Attributes.OperatingSystemEnum.IOS;
import static entity.enums.Attributes.RamEnum.*;

@UtilityClass
public class EntityHandler {

    public static List<PersonalAccountEntity> getPersonalAccountEntities() {
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
        return entities;
    }

    public static List<ItemsEntity> getItemsEntities() {
        List<ItemsEntity> entities = List.of(ItemsEntity.builder()
                        .brand(APPLE)
                        .model("iPhone 14")
                        .internalMemory(GB_512)
                        .ram(GB_8)
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
                        .ram(Attributes.RamEnum.GB_16)
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
                        .ram(Attributes.RamEnum.GB_16)
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
                        .ram(GB_8)
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
                        .ram(GB_4)
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
                        .ram(GB_3)
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
                        .ram(GB_12)
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
                        .ram(Attributes.RamEnum.GB_16)
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
                        .ram(GB_2)
                        .color("brown")
                        .os(ANDROID)
                        .itemSalesInformation(ItemSalesInformationEntity.builder()
                                .price(5_999.99)
                                .currency(CurrencyEnum.₽)
                                .quantity(99)
                                .build())
                        .build());
        return entities;
    }

    public static List<SellHistoryEntity> getSellHistoryEntities() {
        List<SellHistoryEntity> entities = List.of(SellHistoryEntity.builder()
                        .sellDate(OffsetDateTime.now()
                                .minus(3L, ChronoUnit.MONTHS))
                        .user(PersonalAccountEntity.builder()
                                .build())
                        .itemId(ItemsEntity.builder()
                                .build())
                        .quantity(2)
                        .build(),
                SellHistoryEntity.builder()
                        .sellDate(OffsetDateTime.now())
                        .user(PersonalAccountEntity.builder()
                                .build())
                        .itemId(ItemsEntity.builder()
                                .build())
                        .quantity(3)
                        .build(),
                SellHistoryEntity.builder()
                        .sellDate(OffsetDateTime.now()
                                .minus(2L, ChronoUnit.MONTHS))
                        .user(PersonalAccountEntity.builder()
                                .build())
                        .itemId(ItemsEntity.builder()
                                .build())
                        .quantity(10)
                        .build());
        return entities;
    }

    public static <T> void persistEntity(T entity, Session session) {
        session.beginTransaction();
        session.persist(entity);
        session.getTransaction()
                .commit();
    }

    public static <T> void persistEntitiesList(List<T> list, Session session) {
        list.stream()
                .map(element -> {
                    session.persist(element);
                    return element;
                })
                .collect(Collectors.toList());
    }

    public static <T> void dropEntity(T entity, Session session) {
        session.beginTransaction();
        session.remove(entity);
        session.getTransaction()
                .commit();
    }

    public static void dropEntities(Session session, List<? extends BaseEntity>... lists) {
        session.beginTransaction();
        for (List entities : lists) {
            entities.stream()
                    .forEach(entity -> session.remove(entity));
        }
        session.getTransaction()
                .commit();
    }

    public static List<SellHistoryEntity> createSellHistoryEntitiesList(List<PersonalAccountEntity> accounts,
                                                                        List<ItemsEntity> items,
                                                                        List<SellHistoryEntity> sells, int quantity) {
        List<SellHistoryEntity> result = new ArrayList<>();
        for (int quantityCheck = 0, itemIndex = 0, accountIndex = 0, sellIndex = 0; quantityCheck < quantity;
             quantityCheck++, itemIndex++, accountIndex++, sellIndex++) {
            if (accountIndex == accounts.size() - 1) {
                accountIndex = 0;
            }
            if (itemIndex == items.size() - 1) {
                itemIndex = 0;
            }
            if (sellIndex == sells.size() - 1) {
                sellIndex = 0;
            }
            var personalAccountEntity = accounts.get(accountIndex);
            var itemsEntity = items.get(itemIndex);
            var cloneSellHistory = sells.get(sellIndex)
                    .clone();
            cloneSellHistory.setUser(personalAccountEntity);
            cloneSellHistory.setItemId(itemsEntity);
            cloneSellHistory.setPrice(itemsEntity.getItemSalesInformation().getPrice());
            result.add(cloneSellHistory);
        }
        return result;
    }
}
