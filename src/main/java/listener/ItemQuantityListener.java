package listener;

import entity.ItemsEntity;
import entity.SellHistoryEntity;
import jakarta.persistence.LockModeType;
import org.hibernate.event.spi.EventSource;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import utlis.RetryCommand;

public class ItemQuantityListener implements PreInsertEventListener {
    private static RetryCommand<ItemsEntity> retryCommand = new RetryCommand<>(5);

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        if (event.getEntity()
                .getClass() == SellHistoryEntity.class) {
            EventSource session = event.getSession();
            SellHistoryEntity sellHistoryEntity = (SellHistoryEntity) event.getEntity();
            ItemsEntity itemFromSellHistory = sellHistoryEntity.getItemId();
            ItemsEntity itemFromDB = retryCommand.run(() -> session.find(ItemsEntity.class,
                    itemFromSellHistory.getId(), LockModeType.OPTIMISTIC));
            if (sellHistoryEntity.getQuantity() > itemFromDB.getItemSalesInformation().getQuantity()) {
                throw new RuntimeException("На складе нет такого количества товара, доступно: " + itemFromSellHistory.getItemSalesInformation().getQuantity() + " шт " + itemFromSellHistory.getBrand() + " " + itemFromSellHistory.getModel());
            } else {
                itemFromDB.getItemSalesInformation().setQuantity(itemFromDB.getItemSalesInformation().getQuantity() - sellHistoryEntity.getQuantity());
            }
        }
        return false;
    }

}
