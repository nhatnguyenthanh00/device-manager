package com.example.server.service.iService;

import com.example.server.model.Device;

public interface ISimpleEntity<T> {
    T save(T entity);
    void delete(T entity);

}
