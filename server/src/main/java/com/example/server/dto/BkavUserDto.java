package com.example.server.dto;

import com.example.server.model.BkavUser;
import com.example.server.model.enums.Gender;
import com.example.server.model.enums.Role;
import lombok.Data;

@Data
public class BkavUserDto {
    private String username;
    private String name;
    private Role role;
    private Gender gender;

    public BkavUserDto(BkavUser bkavUser) {
        this.username = bkavUser.getUsername();
        this.name = bkavUser.getName();
        this.role = bkavUser.getRole();
        this.gender = bkavUser.getGender();
    }
}
