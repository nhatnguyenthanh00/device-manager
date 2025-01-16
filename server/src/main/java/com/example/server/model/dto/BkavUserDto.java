package com.example.server.model.dto;

import com.example.server.model.entity.BkavUser;
import com.example.server.utils.enums.Gender;
import com.example.server.utils.enums.Role;
import lombok.Data;

@Data
public class BkavUserDto {
    private String id;
    private String username;
    private String name;
    private Role role;
    private Gender gender;
    private int totalDevice;

    public BkavUserDto(BkavUser bkavUser) {
        this.id = String.valueOf(bkavUser.getId());
        this.username = bkavUser.getUsername();
        this.name = bkavUser.getName();
        this.role = bkavUser.getRole();
        this.gender = bkavUser.getGender();
        this.totalDevice = bkavUser.getDevices().size();
    }
}
