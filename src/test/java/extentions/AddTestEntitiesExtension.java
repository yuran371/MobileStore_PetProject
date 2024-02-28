package extentions;

import dao.DaoTestFields;
import entity.ItemsEntity;
import entity.PersonalAccountEntity;
import entity.SellHistoryEntity;
import lombok.Cleanup;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import util.EntityHandler;

import java.util.List;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import static org.junit.jupiter.api.extension.ExtensionContext.Store;

public class AddTestEntitiesExtension implements BeforeAllCallback, AfterAllCallback {
    private final List<SellHistoryEntity> BASIC_ENTITIES = EntityHandler.getSellHistoryEntities();
    private static final Class<SessionFactory> SESSION_FACTORY_CLASS = DaoTestResolver.SESSION_FACTORY_CLASS;
    private List<ItemsEntity> itemsEntitiesList;
    private List<SellHistoryEntity> sellHistoryEntitiesList;
    private List<PersonalAccountEntity> personalAccountEntitiesList;
    private SessionFactory sessionFactory;


    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        Store store = context.getStore(Namespace.create(SESSION_FACTORY_CLASS.getSimpleName()));
        sessionFactory = (SessionFactory) store.get(SESSION_FACTORY_CLASS);
        itemsEntitiesList = EntityHandler.getItemsEntities();
        personalAccountEntitiesList = EntityHandler.getPersonalAccountEntities();
        @Cleanup var session = sessionFactory.openSession();
        EntityHandler.persistEntitiesList(itemsEntitiesList, session);
        EntityHandler.persistEntitiesList(personalAccountEntitiesList, session);
        sellHistoryEntitiesList = EntityHandler.createSellHistoryEntitiesList(personalAccountEntitiesList,
                                                                              itemsEntitiesList,
                                                                              BASIC_ENTITIES, 14);
        EntityHandler.persistEntitiesList(sellHistoryEntitiesList, session);
        context.getTestInstance().ifPresent(testInstance -> {
            DaoTestFields baseDao = (DaoTestFields) testInstance;
            baseDao.itemsEntities = itemsEntitiesList;
            baseDao.personalAccountEntities = personalAccountEntitiesList;
            baseDao.sellHistoryEntities = sellHistoryEntitiesList;
        });
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        @Cleanup var session = sessionFactory.openSession();
        EntityHandler.dropEntities(session, sellHistoryEntitiesList, personalAccountEntitiesList, itemsEntitiesList);
        sessionFactory.close();
    }
}
