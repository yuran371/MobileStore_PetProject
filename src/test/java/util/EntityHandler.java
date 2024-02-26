package util;

import entity.*;
import entity.enums.CountryEnum;
import entity.enums.CurrencyEnum;
import entity.enums.GenderEnum;
import lombok.experimental.UtilityClass;
import org.hibernate.Session;

import java.time.LocalDate;
import java.time.OffsetDateTime;
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
        List<PersonalAccountEntity> entities = List.of(PersonalAccountEntity.builder().image("").name("Artem")
                                                               .surname("Eranov").email("sobaka@mail.ru")
                                                               .birthday(LocalDate.of(1990, 12, 12)).city("Oren")
                                                               .address("Pushkina").countryEnum(CountryEnum.KAZAKHSTAN)
                                                               .genderEnum(GenderEnum.MALE).phoneNumber("+79553330987")
                                                               .password("1499")
                                                               .build(),
                                                       PersonalAccountEntity.builder().image("")
                                                               .name("Danil").surname("Smirnov").email("ds_12@mail.ru")
                                                               .birthday(LocalDate.of(2000, 3, 10)).city("Spb")
                                                               .address("Lenina, b. 18").countryEnum(CountryEnum.RUSSIA)
                                                               .genderEnum(GenderEnum.MALE).phoneNumber("+79553330987")
                                                               .password("FNIM912KND")
                                                               .build(),
                                                       PersonalAccountEntity.builder().image("")
                                                               .name("Dmitry").surname("Eranov").email("dmitry@mail.ru")
                                                               .birthday(LocalDate.of(1997, 12, 20)).city("Minsk")
                                                               .address("Pushkina").countryEnum(CountryEnum.BELARUS)
                                                               .genderEnum(GenderEnum.MALE).phoneNumber("+79553330987")
                                                               .password("Eranoff21").build());
        return entities;
    }

    public static List<ItemsEntity> getItemsEntities() {
        List<ItemsEntity> entities = List.of(ItemsEntity.builder().brand(APPLE).model("iPhone 14")
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
                                                     .os(ANDROID).price(8_999.99).currency(CurrencyEnum.₽).quantity(223)
                                                     .build(), ItemsEntity.builder().brand(SAMSUNG)
                                                     .model("Galaxy S21 FE").internalMemory(GB_128).ram(gb_12)
                                                     .color("yellow").os(ANDROID).price(28_999.99)
                                                     .currency(CurrencyEnum.₽).quantity(99)
                                                     .build(), ItemsEntity.builder().brand(SAMSUNG)
                                                     .model("Galaxy S23 Ultra").internalMemory(GB_1024).ram(gb_16)
                                                     .color("white").os(ANDROID).price(119_999.99)
                                                     .currency(CurrencyEnum.₽).quantity(8)
                                                     .build(), ItemsEntity.builder().brand(SAMSUNG).model("Galaxy A04")
                                                     .internalMemory(GB_32).ram(gb_2).color("brown").os(ANDROID)
                                                     .price(5_999.99).currency(CurrencyEnum.₽).quantity(99).build());
        return entities;
    }

    public static List<SellHistoryEntity> getSellHistoryEntities() {
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

        return entities;
    }

    public static List<ProfileInfoEntity> getProfileInfoEntities() {
        List<ProfileInfoEntity> entities = List.of(ProfileInfoEntity.builder()
                                                           .specialInfo("Delivery only from 2 to 6 p.m")
                                                           .language("Russian").build(),
                                                   ProfileInfoEntity.builder()
                                                           .specialInfo("Before delivery need to call on personal " +
                                                                                "phone")
                                                           .language("Kazakh")
                                                           .build(),
                                                   ProfileInfoEntity.builder().specialInfo("Dont use delivery")
                                                           .language("Ukrainian").build());

        return entities;
    }

    public static <T> void persistEntity(T entity, Session session) {
        session.beginTransaction();
        session.persist(entity);
        session.getTransaction().commit();
    }

    public static <T> void persistEntitiesList(List<T> list, Session session) {
        list.stream().map(element -> {
//            session.beginTransaction();
            session.persist(element);
//            session.getTransaction().commit();
            return element;
        }).collect(Collectors.toList());
    }

    public static <T> void dropEntity(T entity, Session session) {
        session.beginTransaction();
        session.remove(entity);
        session.getTransaction().commit();
    }

    public static void dropEntities(Session session, List<? extends BaseEntity>... lists) {
        session.beginTransaction();
        for (List entities : lists) {
            entities.stream().forEach(entity -> session.remove(entity));
        }
        session.getTransaction().commit();
    }
}
