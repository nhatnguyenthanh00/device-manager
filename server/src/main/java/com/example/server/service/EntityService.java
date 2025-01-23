package com.example.server.service;

import com.example.server.model.response.SampleResponse;

import java.util.UUID;

public interface EntityService<T> {
    SampleResponse<T> save(T entity);
    SampleResponse<Boolean> deleteById(UUID id);

}
