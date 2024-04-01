package factory;

import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Produces;
import org.hibernate.SessionFactory;
import utlis.HibernateSessionFactory;

@Default
public class SessionManagerFactory {

    @Produces
    public SessionFactory getSessionFactory() {
        return HibernateSessionFactory.getSessionFactory();
    }
}
