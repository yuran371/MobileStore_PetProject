package listener;

import entity.ImportantStatisticEntity;
import entity.ItemsEntity;
import entity.PersonalAccountEntity;
import entity.SellHistoryEntity;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.Session;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostInsertEventListener;
import org.hibernate.event.spi.PostUpdateEvent;
import org.hibernate.event.spi.PostUpdateEventListener;
import org.hibernate.persister.entity.EntityPersister;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

public class ImportantStatisticListener implements PostInsertEventListener, PostUpdateEventListener {
    private static final Class<PersonalAccountEntity> personalAccountEntityClass = PersonalAccountEntity.class;
    private static final Class<SellHistoryEntity> sellHistoryEntityClass = SellHistoryEntity.class;
    private static final Class<ItemsEntity> itemsEntityClass = ItemsEntity.class;
    private static final ImportantStatisticEntity statistic = ImportantStatisticEntity.builder().build();

    @Override
    public void onPostInsert(PostInsertEvent event) {
        Class<?> aClass = event.getEntity().getClass();
        if (aClass == personalAccountEntityClass || aClass == sellHistoryEntityClass || aClass == itemsEntityClass) {
            addPersonalStatistic(event.getSession(), aClass);
        }
    }

    @Override
    public boolean requiresPostCommitHandling(EntityPersister persister) {
        return false;
    }

    private static void addPersonalStatistic(Session session, Class<?> clazz) {
        final Map<Class<?>, Consumer<ImportantStatisticEntity>> applyChange =
                Map.of(personalAccountEntityClass,
                       (statisticEntity) -> statisticEntity.setAllUsersCounter(statisticEntity.getAllUsersCounter() + 1),
                       sellHistoryEntityClass,
                       (statisticEntity) -> statisticEntity.setSalesCounter(statisticEntity.getSalesCounter() + 1),
                       itemsEntityClass,
                       (statisticEntity) -> statisticEntity.setItemsCounter(statisticEntity.getItemsCounter() + 1));
        var importantStatisticEntity = session.get(ImportantStatisticEntity.class, statistic.getId(),
                                                   new LockOptions(LockMode.PESSIMISTIC_WRITE, 1));
        applyChange.get(clazz).accept(importantStatisticEntity);
    }

    @Override
    public void onPostUpdate(PostUpdateEvent event) {
        if (event.getEntity().getClass() == personalAccountEntityClass && event.getDirtyProperties().length == 1) {
            int discountEnumIndex = Arrays.asList(event.getPersister().getPropertyNames()).indexOf("discountEnum");
            if (Arrays.stream(event.getDirtyProperties())
                    .anyMatch(value -> value == discountEnumIndex) && event.getOldState()[discountEnumIndex] == null) {
                var importantStatisticEntity = event.getSession().get(ImportantStatisticEntity.class, statistic.getId(),
                                                                      new LockOptions(LockMode.PESSIMISTIC_WRITE, 1));
                importantStatisticEntity.setPremiumUsersCounter(importantStatisticEntity.getPremiumUsersCounter() + 1);
            }
        }
    }

    public ImportantStatisticListener createRowBeforeUseListener(Session session) {
        session.beginTransaction();
        session.persist(statistic);
        session.getTransaction().commit();
        return this;
    }
}


