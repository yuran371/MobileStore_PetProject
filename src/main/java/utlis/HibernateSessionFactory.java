package utlis;

import entity.ItemsEntity;
import entity.PersonalAccountEntity;
import entity.PremiumUserEntity;
import listener.ItemQuantityListener;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;

@UtilityClass
public class HibernateSessionFactory {
    public SessionFactory getSessionFactory() {
        Configuration configuration = buildConfiguration();
        configuration.configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        registerListeners(sessionFactory);
        return sessionFactory;
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(ItemsEntity.class);
        configuration.addAnnotatedClass(PersonalAccountEntity.class);
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(PremiumUserEntity.class);
        return configuration;
    }
    private static void registerListeners(SessionFactory sessionFactory) {
        SessionFactoryImpl unwrap = sessionFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry service = unwrap.getServiceRegistry()
                .getService(EventListenerRegistry.class);
        ItemQuantityListener itemQuantityListener = new ItemQuantityListener();
        service.appendListeners(EventType.PRE_INSERT, itemQuantityListener);
    }
}
