package com.example.server.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;
@Data
@AllArgsConstructor
public class SelectUser {
    UUID id;
    String username;
}
