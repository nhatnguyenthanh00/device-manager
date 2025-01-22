package com.example.server.model.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class DeviceDto {
    private UUID deviceId;
    private String deviceName;
    private String deviceDescription;
    private String deviceCategory;
    private String deviceImage;
    private int deviceStatus;
    private UUID userId;
}
