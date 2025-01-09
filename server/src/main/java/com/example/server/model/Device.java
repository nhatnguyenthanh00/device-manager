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

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 20)
    private DeviceCategory category;
//    @Column(name = "category", nullable = false, length = 20)
//    private String category;

//    @Column(nullable = false)
    private Integer status;  // -1: Unassigned, 0: Assigned, 1: Requested for return

    @ManyToOne(optional = true)
    @JoinColumn(name = "bkav_user_id")
//    @JsonBackReference
    private BkavUser bkavUser;  // Relationship with Account
}
