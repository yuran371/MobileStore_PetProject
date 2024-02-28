package dao;

import com.querydsl.core.Tuple;
import entity.ItemsEntity;
import entity.PersonalAccountEntity;
import entity.SellHistoryEntity;
import entity.enums.CountryEnum;
import entity.enums.GenderEnum;
import extentions.AddTestEntitiesExtension;
import extentions.DaoTestResolver;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import util.EntityHandler;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
@Tag(value = "PersonalAccountDao")
@ExtendWith(value = {DaoTestResolver.class, AddTestEntitiesExtension.class})
public class PersonalAccountDaoTest extends DaoTestFields {
    private final PersonalAccountDao personalAccountDao;
    private final SessionFactory sessionFactory;

    public PersonalAccountDaoTest(PersonalAccountDao instance, SessionFactory sessionFactory) {
        this.personalAccountDao = instance;
        this.sessionFactory = sessionFactory;
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("dao.PersonalAccountDaoTest#argumentsPersonalAccount")
    void insert_NewUser_notNull(PersonalAccountEntity account) {
        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        var insertResult = personalAccountDao.insert(account);
        assertThat(insertResult).isNotEmpty();
        session.remove(account);
        System.out.println(itemsEntities);
        session.getTransaction().commit();
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("dao.PersonalAccountDaoTest#argumentsPersonalAccount")
    void delete_ExistingUser_returnTrue(PersonalAccountEntity account) {
        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(account);
        personalAccountDao.delete(account);
        assertThat(session.get(PersonalAccountEntity.class, account.getId())).isNull();
        session.getTransaction().commit();
    }

    @Tag("Unit")
    @Test
    void getAll_haveUsers_returnAll() {
        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        var resultList = personalAccountDao.getAll();
        assertThat(resultList).hasSameElementsAs(personalAccountEntities);
        session.getTransaction().commit();
    }

    @Tag("Unit")
    @Test
    void getAllWithPhonePurchases_haveUsers_returnAll() {
        var session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        var resultList = personalAccountDao.getAllWithPhonePurchases();
        assertThat(resultList).hasSameElementsAs(personalAccountEntities);
        session.getTransaction().commit();
    }


    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("dao.PersonalAccountDaoTest#argumentsPersonalAccount")
    void validateAuth_validUser_returnUser(PersonalAccountEntity account) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(account);
        Optional<PersonalAccountEntity> personalAccountEntity =
                personalAccountDao.validateAuth(account.getEmail(), account.getPassword());
        assertThat(personalAccountEntity.get()).isNotNull();
        assertThat(personalAccountEntity.get().getEmail()).isEqualTo(account.getEmail());
        assertThat(personalAccountEntity.get().getPassword()).isEqualTo(account.getPassword());
        session.remove(account);
        session.getTransaction().commit();
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("dao.PersonalAccountDaoTest#argumentsPersonalAccount")
    void getByEmail_validUser_returnUser(PersonalAccountEntity account) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(account);
        Optional<PersonalAccountEntity> personalAccountEntity = personalAccountDao.getByEmail(account.getEmail());
        assertThat(personalAccountEntity.get()).isNotNull();
        assertThat(personalAccountEntity.get().getId()).isEqualTo(account.getId());
        assertThat(personalAccountEntity.get().getEmail()).isEqualTo(account.getEmail());
        assertThat(personalAccountEntity.get().getPassword()).isEqualTo(account.getPassword());
        session.remove(account);
        session.getTransaction().commit();
    }


    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("dao.PersonalAccountDaoTest#argumentsPersonalAccount")
    void getAllBoughtPhones_havePhones_returnList(PersonalAccountEntity account) {
        List<SellHistoryEntity> sells = EntityHandler.getSellHistoryEntities().subList(0, 3);
        List<ItemsEntity> items = EntityHandler.getItemsEntities().subList(0, 3);
        @Cleanup Session session = sessionFactory.openSession();
        EntityHandler.persistEntity(account, session);
        EntityHandler.persistEntitiesList(items, session);
        for (int i = 0; i < sells.size(); i++) {
            SellHistoryEntity sellHistoryEntity = sells.get(i);
            sellHistoryEntity.setUser(account);
            sellHistoryEntity.setItemId(items.get(i));
        }
        EntityHandler.persistEntitiesList(sells, session);
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.beginTransaction();
        List<ItemsEntity> allBoughtPhones = personalAccountDao.getAllBoughtPhones(account.getId());
        assertThat(allBoughtPhones.size()).isEqualTo(items.size());
        assertThat(allBoughtPhones).extracting("id")
                .contains(items.get(0).getId(), items.get(1).getId(), items.get(2).getId());
        currentSession.getTransaction().commit();
        EntityHandler.dropEntities(session, sells, items);
        EntityHandler.dropEntity(account, session);
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("dao.PersonalAccountDaoTest#argumentsPersonalAccount")
    void getAllBoughtPhones_noPhones_returnEmptyList(PersonalAccountEntity account) {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        session.persist(account);
        List<ItemsEntity> allBoughtPhones = personalAccountDao.getAllBoughtPhones(account.getId());
        assertThat(allBoughtPhones).isEmpty();
        session.remove(account);
        session.getTransaction().commit();
    }

    @Test
    void getTopTenMostSpenders_haveUsers_returnTop() {
        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();
        List<Tuple> topTenMostSpenders = personalAccountDao.getTopTenMostSpenders();
        for (int i = 1; i < topTenMostSpenders.size(); i++) {
            Double spender = (Double) topTenMostSpenders.get(i - 1).get(1, Double.class);
            Double nextSpender = (Double) topTenMostSpenders.get(i).get(1, Double.class);
            assertThat(spender).isGreaterThan(nextSpender);
        }
        session.getTransaction().commit();
    }

    public static Stream<PersonalAccountEntity> argumentsPersonalAccount() {
        return Stream.of(PersonalAccountEntity.builder().image("").name("Sergei").surname("Elanov")
                                 .email("giga@mail.ru").birthday(LocalDate.of(1990, 12, 12)).city("Moscow")
                                 .address("Pushkina").countryEnum(CountryEnum.RUSSIA)
                                 .genderEnum(GenderEnum.MALE).phoneNumber("+79553330987").password("1499")
                                 .build(),
                         PersonalAccountEntity.builder().image("")
                                 .name("Alan").surname("Kulkaev")
                                 .email("DADA009@mail.ru")
                                 .birthday(LocalDate.of(2000, 3, 10))
                                 .city("Astana").address("Lenina, b. 18")
                                 .countryEnum(CountryEnum.KAZAKHSTAN)
                                 .genderEnum(GenderEnum.MALE)
                                 .phoneNumber("+79553330987")
                                 .password("FNIM912KND")
                                 .build(),
                         PersonalAccountEntity.builder()
                                 .image("")
                                 .name("Elena")
                                 .surname("Mishkina")
                                 .email("lena_mshk@mail.ru")
                                 .birthday(LocalDate.of(1980, 6, 12))
                                 .city("Minsk")
                                 .address("Pushkina")
                                 .countryEnum(CountryEnum.BELARUS)
                                 .genderEnum(GenderEnum.MALE)
                                 .phoneNumber("+79553330987")
                                 .password("lenoff")
                                 .build());

    }
}
