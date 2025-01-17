package com.example.server.model.resquest;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    String oldPassword;
    String newPassword;
}
