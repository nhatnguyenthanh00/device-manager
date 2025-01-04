package com.example.server.model;

import com.example.server.model.enums.DeviceCategory;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String name;

    private String description;

    private String image;

    @Enumerated(EnumType.STRING)
    private DeviceCategory category;

    @Column(nullable = false)
    private int status;  // -1: Unassigned, 0: Assigned, 1: Requested for return

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;  // Relationship with Account
}
