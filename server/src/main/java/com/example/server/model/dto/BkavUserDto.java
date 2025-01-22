package com.example.server.model.dto;

import com.example.server.utils.enums.Gender;
import com.example.server.utils.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class BkavUserDto {
    private UUID userId;
    private String username;
    private String password;
    private String name;
    private Role role;
    private Gender gender;
    private int totalDevice;

}
