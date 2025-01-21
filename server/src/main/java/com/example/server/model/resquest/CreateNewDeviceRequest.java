package com.example.server.model.resquest;

import lombok.Data;

@Data
public class CreateNewDeviceRequest {
    String name;
    String description;
    String category;
    String image;
}
