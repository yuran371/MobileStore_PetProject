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
        @Cleanup SessionFactory entityManager = HibernateTestUtil.getSessionFactory();
        @Cleanup Session session = entityManager.openSession();
        persistEntitiesList(accounts, session);
        persistEntitiesList(items, session);
        Integer quantityBeforePurchase = items.get(0).getItemSalesInformation()
                .getQuantity();
        for (int i = 0; i < 3; i++) {
            items.get(i)
                    .addPhoneOrder(sellHistories.get(i));
            accounts.get(i)
                    .addPurchase(sellHistories.get(i));
        }
        persistEntitiesList(sellHistories, session);
        System.out.println();
        session.beginTransaction();
        Integer quantityItem = session.get(ItemsEntity.class, 1l)
                .getItemSalesInformation()
                .getQuantity();
        Integer quantitySellHistory = session.get(SellHistoryEntity.class, 1l)
                .getQuantity();
        session.getTransaction()
                .commit();
        assertThat(quantityItem).isEqualTo(quantityBeforePurchase - quantitySellHistory);
    }

}
