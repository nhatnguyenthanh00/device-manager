package com.example.server.service;

public interface EntityService<T> {
    T save(T entity);
    void delete(T entity);

}
