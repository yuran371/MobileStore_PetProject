package factory;

import jakarta.enterprise.inject.Produces;
import org.hibernate.SessionFactory;
import utlis.HibernateSessionFactory;

public class SessionManagerFactory {

    @Produces
    public SessionFactory getSessionFactory() {
        return HibernateSessionFactory.getSessionFactory();
    }
}
