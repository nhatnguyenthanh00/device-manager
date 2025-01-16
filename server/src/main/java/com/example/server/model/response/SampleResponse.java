package com.example.server.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SampleResponse<T> {
    private int errCode;
    private T data;
    private String errMsg;

    public SampleResponse(int errCode, T data) {
        this.errCode = errCode;
        this.data = data;
    }
}
