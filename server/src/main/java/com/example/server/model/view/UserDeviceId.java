package com.example.server.model.view;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class UserDeviceId implements Serializable {
    private UUID userId;
    private UUID deviceId;
}
