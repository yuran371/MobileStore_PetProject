package factory;

import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import utlis.HibernateSessionFactory;

import java.lang.reflect.Proxy;

public class EntityManagerFactory {

    @Produces
    public EntityManager getEntityManager() {
        return (Session) Proxy.newProxyInstance(org.hibernate.SessionFactory.class.getClassLoader(),
                                                new Class[]{Session.class},
                                                (proxy, method, args) -> method.invoke(HibernateSessionFactory.getSessionFactory()
                                                                                               .getCurrentSession(),
                                                                                       args));
    }
}
