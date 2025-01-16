package com.example.server.service;

import java.util.UUID;

public interface EntityService<T> {
    T save(T entity);
    boolean deleteById(UUID id);

}
