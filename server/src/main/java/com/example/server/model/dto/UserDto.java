package com.example.server.model.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserDto {
    private UUID userId;
    private String name;
    private String username;
    private String role;
    private String gender;
    private List<DeviceDto> devices;
}
