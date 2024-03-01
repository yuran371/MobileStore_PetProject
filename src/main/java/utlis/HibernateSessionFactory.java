package utlis;

import entity.ImportantStatisticEntity;
import entity.ItemsEntity;
import entity.PersonalAccountEntity;
import listener.ImportantStatisticListener;
import listener.ItemQuantityListener;
import lombok.Cleanup;
import lombok.experimental.UtilityClass;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;

import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.nio.file.Path;

@UtilityClass
public class HibernateSessionFactory {

    private static final Path PATH_TO_CFG = Path.of("src/main/resources/hibernate.cfg.xml");
    private static final SessionFactory SESSION_FACTORY = buildSessionFactory();
    private static SessionFactory buildSessionFactory() {
        Configuration configuration = buildConfiguration();
        configuration.configure(PATH_TO_CFG.toFile());
        CachingProvider caching = Caching.getCachingProvider();

        var sessionFactory = configuration.buildSessionFactory();
        listenerRegistration(sessionFactory);
        return sessionFactory;
    }

    public static void listenerRegistration(SessionFactory sessionFactory) {
        var sessionFactoryImpl = sessionFactory.unwrap(SessionFactoryImpl.class);
        var service = sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);
        @Cleanup var session = sessionFactory.openSession();
        service.appendListeners(EventType.POST_INSERT,
                                new ImportantStatisticListener().createRowBeforeUseListener(session));
        service.appendListeners(EventType.POST_UPDATE, new ImportantStatisticListener());
        service.appendListeners(EventType.PRE_INSERT, new ItemQuantityListener());
    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(ItemsEntity.class);
        configuration.addAnnotatedClass(PersonalAccountEntity.class);
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(ImportantStatisticEntity.class);
        return configuration;
    }

    public static SessionFactory getSessionFactory() {
        return SESSION_FACTORY;
    }
    public static Boolean closeSessionFactory() {
        try {
            SESSION_FACTORY.close();
            return Boolean.TRUE;
        } catch (HibernateException e) {
            return Boolean.FALSE;
        }
    }
}
