package com.orzelowski.spacex.dragons.repository;

import java.util.List;
import java.util.Optional;

public interface LocalRepository<T, ID> {
    List<T> findAll();
    Optional<T> findById(ID id);
    T save(T entity);
    void delete(T entity);

}
