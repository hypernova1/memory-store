package org.sam.store.common.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, U> {
    T save(T t);
    List<T> save(List<T> list);
    List<T> findAll();
    List<T> find(int page, int size);
    Optional<T> findById(U id);
    void delete(T t);
    void delete(List<T> list);
    void deleteById(U id);
}
