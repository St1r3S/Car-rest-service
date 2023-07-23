package ua.foxminded.carrestservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrudService<T, K> {

    T save(T entity);

    List<T> saveAll(List<T> entities);

    T findById(K id);

    boolean existsById(K id);

    List<T> findAll();

    Page<T> findAll(Pageable pageable);

    List<T> findAllById(List<K> ids);

    long count();

    void deleteById(K id);

    void delete(T entity);

    void deleteAllById(List<K> ids);

    void deleteAll(List<T> entities);

    void deleteAll();


}
