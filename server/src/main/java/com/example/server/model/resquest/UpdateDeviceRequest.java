package com.example.server.model.resquest;

import com.example.server.utils.enums.DeviceCategory;
import lombok.Data;

import java.util.UUID;

@Data
public class UpdateDeviceRequest {
    private String deviceId;
    private String name;
    private String description;
    private String image;
    private DeviceCategory category;
    private String userName;
}
