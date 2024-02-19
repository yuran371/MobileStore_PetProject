package listener;

import entity.ItemsEntity;
import entity.PersonalAccountEntity;
import entity.SellHistoryEntity;
import lombok.Cleanup;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import util.EntityHandler;
import util.HibernateTestUtil;
import utlis.HibernateSessionFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;
import static util.EntityHandler.persistEntitiesList;

@TestInstance(PER_METHOD)
@Tag(value = "ItemQuantityListenerTest")
public class ItemQuantityListenerTest {

    @Test
    void isItemQuantityChange_afterInsertSellHistory_true() {
        List<ItemsEntity> items = EntityHandler.getItemsEntities();
        List<PersonalAccountEntity> accounts = EntityHandler.getPersonalAccountEntities();
        List<SellHistoryEntity> sellHistories = EntityHandler.getSellHistoryEntities();
        @Cleanup SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
        @Cleanup Session session = sessionFactory.openSession();
        persistEntitiesList(accounts, session);
        persistEntitiesList(items, session);
        Integer quantityBeforePurchase = items.get(0)
                .getQuantity();
        for (int i = 0; i < 3; i++) {
            items.get(i)
                    .addPhoneOrder(sellHistories.get(i));
            accounts.get(i)
                    .addPurchase(sellHistories.get(i));
        }
        persistEntitiesList(sellHistories, session);
        session.beginTransaction();
        Integer quantityItem = session.get(ItemsEntity.class, 1l)
                .getQuantity();
        Integer quantitySellHistory = session.get(SellHistoryEntity.class, 1l)
                .getQuantity();
        session.getTransaction()
                .commit();
        assertThat(quantityItem).isEqualTo(quantityBeforePurchase - quantitySellHistory);
    }

    @Test
    void isisis() {
        List<ItemsEntity> items = EntityHandler.getItemsEntities();
        List<PersonalAccountEntity> accounts = EntityHandler.getPersonalAccountEntities();
        List<SellHistoryEntity> sellHistories = EntityHandler.getSellHistoryEntities();
        @Cleanup SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        @Cleanup Session session1 = sessionFactory.openSession();
        persistEntitiesList(accounts, session1);
        persistEntitiesList(items, session1);

        Integer quantityBeforePurchase = items.get(0)
                .getQuantity();
//        for (int i = 0; i < 3; i++) {
//            items.get(i)
//                    .addPhoneOrder(sellHistories.get(i));
//            accounts.get(i)
//                    .addPurchase(sellHistories.get(i));
//        }
        items.get(0)
                .addPhoneOrder(sellHistories.get(0));
        accounts.get(0)
                .addPurchase(sellHistories.get(0));
        session1.beginTransaction();
        session1.persist(sellHistories.get(0));
        session1.getTransaction()
                .commit();
        System.out.println(sellHistories.get(0));
        System.out.println(items.get(0));

    }
}
