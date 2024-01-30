import lombok.experimental.UtilityClass;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
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
        configuration.configure();
        return configuration.buildSessionFactory();
    }
}