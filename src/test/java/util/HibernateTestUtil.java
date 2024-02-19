package util;

import listener.ItemQuantityListener;
import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.testcontainers.containers.PostgreSQLContainer;
import utlis.HibernateSessionFactory;

@UtilityClass
public class HibernateTestUtil {

    public static final PostgreSQLContainer<?> testContainer = new PostgreSQLContainer<>("postgres:16");

    static {
        testContainer.start();
    }

    public static SessionFactory getSessionFactory() {
        Configuration configuration = HibernateSessionFactory.buildConfiguration();
        configuration.setProperty("hibernate.connection.url", testContainer.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", testContainer.getUsername());
        configuration.setProperty("hibernate.connection.password", testContainer.getPassword());
        configuration.setProperty("jakarta.persistence.create-database-schemas", "true");
        configuration.setProperty("hibernate.hbm2ddl.import_files", "addPgcrypto.sql");
        configuration.setProperty("hibernate.generate_statistics", "true");
        configuration.configure();
        SessionFactory sessionFactory = configuration.buildSessionFactory();
        registerListeners(sessionFactory);
        return sessionFactory;
    }
    private static void registerListeners(SessionFactory sessionFactory) {
        SessionFactoryImpl unwrap = sessionFactory.unwrap(SessionFactoryImpl.class);
        EventListenerRegistry service = unwrap.getServiceRegistry()
                .getService(EventListenerRegistry.class);
        ItemQuantityListener itemQuantityListener = new ItemQuantityListener();
        service.appendListeners(EventType.PRE_INSERT, itemQuantityListener);
    }
}
