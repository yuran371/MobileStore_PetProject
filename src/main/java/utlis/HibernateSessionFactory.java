package utlis;

import entity.ImportantStatisticEntity;
import entity.ItemsEntity;
import entity.PersonalAccountEntity;
import listener.ImportantStatisticListener;
import listener.SendAuthEmailListener;
import lombok.Cleanup;
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
        var sessionFactory = configuration.buildSessionFactory();
        listenerRegistration(sessionFactory);
        return sessionFactory;
    }

    public static void listenerRegistration(SessionFactory sessionFactory) {
        var sessionFactoryImpl = sessionFactory.unwrap(SessionFactoryImpl.class);
        var service = sessionFactoryImpl.getServiceRegistry().getService(EventListenerRegistry.class);
        @Cleanup var session = sessionFactory.openSession();
        service.appendListeners(EventType.POST_INSERT, new ImportantStatisticListener().createRowBeforeUseListener(session), new SendAuthEmailListener());
        service.appendListeners(EventType.POST_UPDATE, new ImportantStatisticListener());

    }

    public static Configuration buildConfiguration() {
        Configuration configuration = new Configuration();
        configuration.addAnnotatedClass(ItemsEntity.class);
        configuration.addAnnotatedClass(PersonalAccountEntity.class);
        configuration.setPhysicalNamingStrategy(new CamelCaseToUnderscoresNamingStrategy());
        configuration.addAnnotatedClass(ImportantStatisticEntity.class);
        return configuration;
    }
}
