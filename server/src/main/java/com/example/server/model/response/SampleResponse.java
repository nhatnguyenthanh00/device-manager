package com.example.server.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SampleResponse<T> {
    private T data;
    private String errMsg;

    public SampleResponse(T data) {
        this.data = data;
    }
}
