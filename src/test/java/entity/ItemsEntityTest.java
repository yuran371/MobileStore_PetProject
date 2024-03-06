package entity;

import dao.ItemsDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import util.EntityHandler;
import util.HibernateTestUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Proxy;

import static org.assertj.core.api.Assertions.assertThat;
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

    @ParameterizedTest
    @MethodSource("dao.ItemsDaoTest#argumentsWithOneItem")
    void check_Image_true(ItemsEntity itemEntity) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try (InputStream in = ClassLoader.getSystemClassLoader().getResourceAsStream("w200_Test.png")) {
            int length;
            byte[] buffer = new byte[1024];
            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
        }
        byte[] image = out.toByteArray();
        System.out.println("image.length "+image.length);
        SessionFactory sessionFactory = HibernateTestUtil.getSessionFactory();
        Session session = (Session) Proxy.newProxyInstance(SessionFactory.class.getClassLoader(),
                new Class[]{Session.class},
                (proxy, method, args) -> method.invoke(sessionFactory.getCurrentSession(), args));
        ItemsDao itemsDao = new ItemsDao(session);
        itemEntity.setImage(image);
        EntityHandler.persistEntity(itemEntity, session);
        session.beginTransaction();
        byte[] imageFromDB = session.get(ItemsEntity.class, 1L)
                .getImage();
        session.getTransaction().commit();
        assertThat(imageFromDB).isEqualTo(itemEntity.getImage());
    }
}
