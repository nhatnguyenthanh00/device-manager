package com.example.server.resquest;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
