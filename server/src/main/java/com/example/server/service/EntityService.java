package com.example.server.service;

import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface EntityService<T> {
    ResponseEntity<?> save(T entity);
    ResponseEntity<?> deleteById(UUID id);

}
