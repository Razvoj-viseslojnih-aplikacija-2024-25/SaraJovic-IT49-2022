package rva.services;

import java.util.List;
import java.util.Optional;


public interface CrudService <T>{
    List<T> getAll();

    boolean existsById(long id);

    Optional<T> findById(long id);

    T create(T t);

    Optional<T> update(T t, long id);

    void delete(long id);
}
