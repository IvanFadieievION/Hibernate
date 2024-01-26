package org.example.dao;

import java.util.List;
import java.util.Optional;

public interface GenericDao<T> {
    T save(T t);
    Optional<T> find(Long id);
    T update(T t);
    void delete(Long id);
    List<T> findAll();
}
