package com.example.server.model.resquest;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
