package com.example.server.model.entity;

import com.example.server.utils.enums.DeviceCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Data
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 20)
    private DeviceCategory category;

    private Integer status;  // -1: Unassigned, 0: Assigned, 1: Requested for return

    @JoinColumn(name = "bkav_user_id")
    private UUID bkavUserId;
}
