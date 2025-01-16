package com.example.server.repository.dao;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntityDao<T> {
    Optional<T> getById(UUID id);

    List<T> getAll();

    T save(T t);

    void deleteById(UUID id);
}
