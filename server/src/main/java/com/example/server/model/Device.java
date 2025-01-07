package com.example.server.model;

import com.example.server.model.enums.DeviceCategory;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    private String description;

    private String image;

    @Enumerated(EnumType.STRING)
    private DeviceCategory category;

    @Column(nullable = false)
    private int status;  // -1: Unassigned, 0: Assigned, 1: Requested for return

    @ManyToOne
    @JoinColumn(name = "bkav_user_id")
    @JsonBackReference
    private BkavUser bkavUser;  // Relationship with Account
}
