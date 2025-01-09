package com.example.server.model.view;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.UUID;


@Data
@Entity
public class BkavUserDeviceView {

    @Id
    private String id;
//    @Column(name = "user_id")
    private UUID userId;

    private String name;

    private String username;

    private String role;

    private String gender;

//    @Column(name = "device_id")
    private UUID deviceId;

    @Column(name = "device_name")
    private String deviceName;

    @Column(name = "device_description")
    private String deviceDescription;

    @Column(name = "device_category")
    private String deviceCategory;

    @Column(name = "device_status")
    private Integer deviceStatus;
}
