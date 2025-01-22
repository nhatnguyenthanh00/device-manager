package com.example.server.model.resquest;

import lombok.Data;

import java.util.UUID;

@Data
public class ResetPasswordRequest {
    UUID userId;
    String password;
    String adminPassword;
}
