package dao;

import com.querydsl.core.Tuple;
import entity.*;
import entity.enums.DiscountEnum;
import extentions.PersonalAccountParameterResolver;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import util.EntityHandler;
import util.HibernateTestUtil;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;

@TestInstance(PER_METHOD)
@Tag(value = "PersonalAccountDao")
@ExtendWith(value = {PersonalAccountParameterResolver.class})
public class PersonalAccountDaoTest {
    private final PersonalAccountDao personalAccountDao;
    private static final SessionFactory entityManager = HibernateTestUtil.getSessionFactory();

    public PersonalAccountDaoTest(PersonalAccountDao instance) {
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
    @Test
    void getAll_haveUsers_returnAll() {
        var personalAccountEntities = EntityHandler.getPersonalAccountEntities();
        var profileInfoEntities = EntityHandler.getProfileInfoEntities();
        for (int i = 0, j = 0; (i < profileInfoEntities.size() & j < personalAccountEntities.size()); i++, j++) {
            ProfileInfoEntity profileInfoEntity = profileInfoEntities.get(i);
            var personalAccountEntity = personalAccountEntities.get(j);
            profileInfoEntity.setPersonalAccount(personalAccountEntity);
        }
        @Cleanup var session = entityManager.openSession();
        EntityHandler.persistEntitiesList(personalAccountEntities, session);
        session.clear();
        var resultList = personalAccountDao.getAll(session);
        assertThat(resultList).hasSameElementsAs(personalAccountEntities);
        EntityHandler.dropEntities(session, resultList);
    }

    @Tag("Unit")
    @Test
    void getAllWithPhonePurchases_haveUsers_returnAll() {
        var personalAccountEntities = EntityHandler.getPersonalAccountEntities();
        var profileInfoEntities = EntityHandler.getProfileInfoEntities();
        for (int i = 0, j = 0; (i < profileInfoEntities.size() & j < personalAccountEntities.size()); i++, j++) {
            ProfileInfoEntity profileInfoEntity = profileInfoEntities.get(i);
            var personalAccountEntity = personalAccountEntities.get(j);
            profileInfoEntity.setPersonalAccount(personalAccountEntity);
        }
        @Cleanup var session = entityManager.openSession();
        EntityHandler.persistEntitiesList(personalAccountEntities, session);
        session.clear();
        var resultList = personalAccountDao.getAllWithPhonePurchases(session);
        EntityHandler.dropEntities(session, resultList);
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("unit.DaoTest#argumentsPersonalAccount")
    void validateAuth_validUser_returnUser(PersonalAccountEntity account) {
        @Cleanup Session session = entityManager.openSession();
        EntityHandler.persistEntity(account, session);
        Optional<PersonalAccountEntity> personalAccountEntity =
                personalAccountDao.validateAuth(account.getEmail(), account.getPassword(), session);
        assertThat(personalAccountEntity.get()).isNotNull();
        assertThat(personalAccountEntity.get().getEmail()).isEqualTo(account.getEmail());
        assertThat(personalAccountEntity.get().getPassword()).isEqualTo(account.getPassword());
        EntityHandler.dropEntity(account, session);
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("unit.DaoTest#argumentsPersonalAccount")
    void getByEmail_validUser_returnUser(PersonalAccountEntity account) {
        @Cleanup Session session = entityManager.openSession();
        EntityHandler.persistEntity(account, session);
        Optional<PersonalAccountEntity> personalAccountEntity = personalAccountDao.getByEmail(account.getEmail(),
                                                                                              session);
        assertThat(personalAccountEntity.get()).isNotNull();
        assertThat(personalAccountEntity.get().getId()).isEqualTo(account.getId());
        assertThat(personalAccountEntity.get().getEmail()).isEqualTo(account.getEmail());
        assertThat(personalAccountEntity.get().getPassword()).isEqualTo(account.getPassword());
        EntityHandler.dropEntity(account, session);
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("unit.DaoTest#argumentsPersonalAccount")
    void checkDiscount_premiumUser_returnDiscount(PersonalAccountEntity account) {
        @Cleanup Session session = entityManager.openSession();
        PremiumUserEntity premiumUserEntity = new PremiumUserEntity(account, DiscountEnum.FIVE_PERCENT);
        EntityHandler.persistEntity(premiumUserEntity, session);
        Optional<DiscountEnum> discount = personalAccountDao.checkDiscount(premiumUserEntity.getId(), session);
        assertThat(discount.get()).isEqualTo(DiscountEnum.FIVE_PERCENT);
        EntityHandler.dropEntity(premiumUserEntity, session);
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("unit.DaoTest#argumentsPersonalAccount")
    void checkDiscount_notPremiumUser_returnNull(PersonalAccountEntity account) {
        @Cleanup Session session = entityManager.openSession();
        EntityHandler.persistEntity(account, session);
        Optional<DiscountEnum> discount = personalAccountDao.checkDiscount(account.getId(), session);
        assertThat(discount).isEmpty();
        EntityHandler.dropEntity(account, session);
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("unit.DaoTest#argumentsPersonalAccount")
    void getAllBoughtPhones_havePhones_returnList(PersonalAccountEntity account) {
        List<SellHistoryEntity> sellHistoryEntities = EntityHandler.getSellHistoryEntities();
        List<ItemsEntity> itemsEntities = EntityHandler.getItemsEntities();
        @Cleanup Session session = entityManager.openSession();
        EntityHandler.persistEntity(account, session);
        EntityHandler.persistEntitiesList(itemsEntities, session);
        for (int i = 0; i < sellHistoryEntities.size(); i++) {
            SellHistoryEntity sellHistoryEntity = sellHistoryEntities.get(i);
            sellHistoryEntity.setUser(account);
            sellHistoryEntity.setItemId(itemsEntities.get(i));
        }
        EntityHandler.persistEntitiesList(sellHistoryEntities, session);
        List<ItemsEntity> allBoughtPhones = personalAccountDao.getAllBoughtPhones(account.getId(), session);
        assertThat(allBoughtPhones.size()).isEqualTo(itemsEntities.size());
        assertThat(allBoughtPhones).extracting("id")
                .contains(itemsEntities.get(0).getId(), itemsEntities.get(1).getId(), itemsEntities.get(2).getId());
        EntityHandler.dropEntities(session, sellHistoryEntities, itemsEntities);
        EntityHandler.dropEntity(account, session);
    }

    @Tag("Unit")
    @ParameterizedTest
    @MethodSource("unit.DaoTest#argumentsPersonalAccount")
    void getAllBoughtPhones_noPhones_returnEmptyList(PersonalAccountEntity account) {
        @Cleanup Session session = entityManager.openSession();
        EntityHandler.persistEntity(account, session);
        List<ItemsEntity> allBoughtPhones = personalAccountDao.getAllBoughtPhones(account.getId(), session);
        assertThat(allBoughtPhones).isEmpty();
        EntityHandler.dropEntity(account, session);
    }

    @Test
    void getTopTenMostSpenders_haveUsers_returnTop() {
        List<PersonalAccountEntity> accounts = EntityHandler.getPersonalAccountEntities();
        List<ItemsEntity> items = EntityHandler.getItemsEntities();
        List<SellHistoryEntity> sellHistoryEntities = EntityHandler.getSellHistoryEntities();
        @Cleanup Session session = entityManager.openSession();
        EntityHandler.persistEntitiesList(accounts, session);
        EntityHandler.persistEntitiesList(items, session);
        for (SellHistoryEntity entity : sellHistoryEntities) {
            Random random = new Random();
            entity.setUser(accounts.get(random.nextInt(0, accounts.size())));
            entity.setItemId(items.get(random.nextInt(0, items.size())));
        }
        EntityHandler.persistEntitiesList(sellHistoryEntities, session);
        List<Tuple> topTenMostSpenders = personalAccountDao.getTopTenMostSpenders(session);
        for (int i = 1; i < topTenMostSpenders.size(); i++) {
            Double spender = (Double) topTenMostSpenders.get(i - 1).get(1, Double.class);
            Double nextSpender = (Double) topTenMostSpenders.get(i).get(1, Double.class);
            assertThat(spender).isGreaterThan(nextSpender);
        }
        EntityHandler.dropEntities(session, accounts, items, sellHistoryEntities);
    }

    @Test
    void sortByGenderAndCountry_haveUsers_returnFilteredUsers() {
        EntityHandler.getProfileInfoEntities();
    }
}
