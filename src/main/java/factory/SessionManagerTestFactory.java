package factory;

import jakarta.enterprise.inject.Alternative;
import jakarta.enterprise.inject.Produces;
import org.hibernate.SessionFactory;
import utlis.HibernateTestUtil;

@Alternative
public class SessionManagerTestFactory {

    @Produces
    public SessionFactory getSessionFactory() {
        return HibernateTestUtil.sessionFactory;
    }
}
