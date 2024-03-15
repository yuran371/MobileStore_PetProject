package factory;

import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import utlis.HibernateTestUtil;

import java.lang.reflect.Proxy;

@Alternative
public class EntityManagerTestFactory {

    @Produces
    public EntityManager getEntityManager() {
        return (Session) Proxy.newProxyInstance(org.hibernate.SessionFactory.class.getClassLoader(),
                                                new Class[]{Session.class},
                                                (proxy, method, args) -> method.invoke(HibernateTestUtil.sessionFactory.getCurrentSession(),
                                                                                       args));
    }
}
