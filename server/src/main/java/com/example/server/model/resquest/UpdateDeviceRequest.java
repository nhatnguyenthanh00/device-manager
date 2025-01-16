package com.example.server.model.resquest;

import com.example.server.utils.enums.DeviceCategory;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateDeviceRequest {
    private UUID deviceId;
    private String name;
    private String description;
    private String image;
    private DeviceCategory category;
//    private String category;
    private int status;
    private UUID userId;
}
