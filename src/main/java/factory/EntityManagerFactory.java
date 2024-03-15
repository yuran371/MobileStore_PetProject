package factory;

import jakarta.enterprise.inject.Produces;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import utlis.HibernateSessionFactory;

import java.lang.reflect.Proxy;

public class EntityManagerFactory {

    @Produces
    public Session getEntityManager() {
        SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
        return (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[]{Session.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));
    }

}
