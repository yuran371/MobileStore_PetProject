package dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T, E> {

	Optional<T> insert(E entity);

	List<E> findAll();

	Optional<E> getById(T id);

	boolean delete(T params);
}
