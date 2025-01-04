package com.example.server.resquest;

import com.example.server.model.enums.DeviceCategory;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateDeviceRequest {
    private UUID deviceId;
    private String name;
    private String description;
    private String image;
    private DeviceCategory category;
    private int status;
    private UUID accountId;
}
