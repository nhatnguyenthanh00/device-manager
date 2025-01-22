package com.example.server.service;

import java.util.UUID;

public interface EntityService<T> {
    T save(T entity) throws Exception;
    boolean deleteById(UUID id);

}
