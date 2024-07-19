package entity;

import dao.ItemsDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import util.EntityHandler;
import utlis.HibernateTestUtil;

import java.lang.reflect.Proxy;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_METHOD;

@TestInstance(PER_METHOD)
@Tag(value = "ItemsEntityTest")
public class ItemsEntityTest {
    @ParameterizedTest
    @MethodSource("dao.ItemsDaoTest#argumentsWithOneItem")
    void second_level_cache(ItemsEntity itemEntity) {
        SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
        Session session1 = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[]{Session.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));
        Session session2 = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[]{Session.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));
        ItemsDao itemsDao = new ItemsDao(session1);
        EntityHandler.persistEntity(itemEntity, session1);
        session1.beginTransaction();
        ItemsEntity itemsEntity1 = session1.get(ItemsEntity.class, 1l);
        ItemsEntity itemsEntity1_2 = session1.get(ItemsEntity.class, 1l);
        session1.getTransaction().commit();
        session2.beginTransaction();
        ItemsEntity itemsEntity2 = session2.get(ItemsEntity.class, 1l);
        session2.getTransaction().commit();
    }

}
