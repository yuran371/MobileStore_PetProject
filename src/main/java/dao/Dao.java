package dao;

import entity.BaseEntity;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.emptyMap;

public interface Dao<K extends Serializable, E extends BaseEntity<K>> {

	Optional<E> insert(E entity);

	default Optional<E> getById(K id) {
		return getById(id, emptyMap());
	}

	Optional<E> getById(K id, Map<String, Object> properties);
	void delete(E entity);

	void update(E entity);
}
