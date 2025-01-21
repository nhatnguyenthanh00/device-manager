package com.example.server.model.dto;

import com.example.server.model.entity.BkavUser;
import com.example.server.utils.enums.Gender;
import com.example.server.utils.enums.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BkavUserDto {
    private String id;
    private String username;
    private String name;
    private Role role;
    private Gender gender;
    private int totalDevice;

}
