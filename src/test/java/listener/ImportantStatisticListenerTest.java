package listener;

import dao.PersonalAccountDao;
import entity.ImportantStatisticEntity;
import entity.ItemsEntity;
import entity.PersonalAccountEntity;
import entity.SellHistoryEntity;
import lombok.Cleanup;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.*;
import util.EntityHandler;
import util.HibernateTestUtil;

import java.util.List;

import static entity.enums.DiscountEnum.FIVE_PERCENT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;

@TestInstance(PER_METHOD)
@Tag(value = "ImportantStatisticListener")
//@ExtendWith(value = {PersonalAccountParameterResolver.class})
public class ImportantStatisticListenerTest {

    private final PersonalAccountDao personalAccountDao;
    private static List<ItemsEntity> itemsEntities;
    private static List<SellHistoryEntity> sellHistoryEntities;
    private static List<PersonalAccountEntity> personalAccountEntities;
    private static final SessionFactory entityManager = HibernateTestUtil.getSessionFactory();

    public ImportantStatisticListenerTest(PersonalAccountDao personalAccountDao) {
        this.personalAccountDao = personalAccountDao;
    }

    @BeforeAll
    public static void fillTableWithEntities() {
        itemsEntities = EntityHandler.getItemsEntities();
        personalAccountEntities = EntityHandler.getPersonalAccountEntities();
        var basicEntities = EntityHandler.getSellHistoryEntities();
        @Cleanup var session = entityManager.openSession();
        EntityHandler.persistEntitiesList(itemsEntities, session);
        EntityHandler.persistEntitiesList(personalAccountEntities, session);
        sellHistoryEntities = EntityHandler.createSellHistoryEntitiesList(personalAccountEntities, itemsEntities,
                                                                          basicEntities, 14);
        EntityHandler.persistEntitiesList(sellHistoryEntities, session);
    }

    @AfterAll
    public static void closeTestSessionFactory() {
        @Cleanup var session = entityManager.openSession();
        EntityHandler.dropEntities(session, sellHistoryEntities, personalAccountEntities, itemsEntities);
        entityManager.close();
    }

    @Tag("Unit")
    @Test
    void checkStatistic_addEntities_StatisticCreated() {
        @Cleanup var session = entityManager.openSession();
        session.beginTransaction();
        var importantStatisticEntity = session.get(ImportantStatisticEntity.class, 1L);
        assertThat(importantStatisticEntity.getItemsCounter()).isEqualTo(itemsEntities.size());
        assertThat(importantStatisticEntity.getSalesCounter()).isEqualTo(sellHistoryEntities.size());
        assertThat(importantStatisticEntity.getAllUsersCounter()).isEqualTo(personalAccountEntities.size());
        session.getTransaction().commit();
    }

    @Tag("Unit")
    @Test
    void checkPremiumAcc_addPremiumEntities_increaseNumber() {
        personalAccountEntities.stream().forEach(account -> account.setDiscountEnum(FIVE_PERCENT));
        @Cleanup var session = entityManager.openSession();
        for (PersonalAccountEntity premiumUserEntity : personalAccountEntities) {
            session.beginTransaction();
            session.merge(premiumUserEntity);
            session.getTransaction().commit();
        }
        session.beginTransaction();
        var importantStatisticEntity = session.get(ImportantStatisticEntity.class, 1L);
        assertThat(importantStatisticEntity.getPremiumUsersCounter()).isEqualTo(personalAccountEntities.size());
        session.getTransaction().commit();

    }
}