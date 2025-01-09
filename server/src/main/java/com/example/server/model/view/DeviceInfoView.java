package com.example.server.model.view;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;

@Data
@Entity
public class DeviceInfoView{
    @Id
    @Column(name = "device_id")
    private UUID deviceId;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "device_description")
    private String deviceDescription;

    @Column(name = "device_image")
    private String deviceImage;

    @Column(name = "device_category")
    private String deviceCategory;

    @Column(name = "device_status")
    private int deviceStatus;

    @Column(name = "user_username")
    private String userUsername;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_role")
    private String userRole;
}
