package dao;

import entity.BaseEntity;

import java.io.Serializable;
import java.util.Optional;

public interface Dao<K extends Serializable, E extends BaseEntity<K>> {

	Optional<E> insert(E entity);

	Optional<E> getById(K id);

	void delete(K id);

	void update(E entity);
}
