package dao;

import entity.BaseEntity;
import jakarta.persistence.EntityManager;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class DaoBase<K extends Serializable, E extends BaseEntity<K>> implements Dao<K, E> {

    @Getter
    private final EntityManager entityManager;
    private final Class<E> clazz;

    @Override
    public Optional<E> insert(E entity) {
        entityManager.persist(entity);
        return Optional.ofNullable(entity);
    }

    @Override
    public void delete(K id) {
        entityManager.remove(id);
        entityManager.flush();
    }

    @Override
    public void update(E entity) {
        entityManager.merge(entity);
    }

    @Override
    public Optional<E> getById(K id) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

}
