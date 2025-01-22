package com.example.server.model.dto;

import com.example.server.utils.enums.DeviceCategory;
import lombok.Data;

import java.util.UUID;

@Data
public class DeviceDto {
    private UUID id;
    private String name;
    private String description;
    private String image;
    private DeviceCategory category;
    private Integer status;
    private UUID userId;
}
