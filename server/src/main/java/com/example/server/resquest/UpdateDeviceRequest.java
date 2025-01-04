package com.example.server.resquest;

import com.example.server.model.enums.DeviceCategory;
import lombok.Data;

@Data
public class UpdateDeviceRequest {
    private Integer deviceId;
    private String name;
    private String description;
    private String image;
    private DeviceCategory category;
    private int status;
    private Integer accountId;
}
