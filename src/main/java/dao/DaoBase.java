package dao;

import com.querydsl.core.types.EntityPath;
import com.querydsl.jpa.impl.JPAQuery;
import entity.BaseEntity;
import entity.ItemsEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DaoBase<K extends Serializable, E extends BaseEntity<K>> implements Dao<K, E> {
    private final EntityPath<E> qClazz;
    private final SessionFactory sessionFactory;
    private final Class<E> clazz;
    @Override
    public Optional<E> insert(E entity) {
        Session session = sessionFactory.getCurrentSession();
        session.persist(entity);
        return Optional.ofNullable(entity);
    }

    @Override
    public void delete(K id) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(id);
        session.flush();
    }

    @Override
    public void update(E entity) {
        Session session = sessionFactory.getCurrentSession();
        session.merge(entity);
    }

    @Override
    public Optional<E> getById(K id) {
        Session session = sessionFactory.getCurrentSession();
        return Optional.ofNullable(session.find(clazz, id));
    }

    @Override
    public List<E> findAll() {
        Session session = sessionFactory.getCurrentSession();
        return new JPAQuery<ItemsEntity>(session).select(qClazz)
                .from(qClazz)
                .fetch();
    }
}
