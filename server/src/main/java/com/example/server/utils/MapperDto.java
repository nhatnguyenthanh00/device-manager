package com.example.server.utils;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.entity.BkavUser;
import com.example.server.repository.view.DeviceInfoViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapperDto {
    @Autowired
    private DeviceInfoViewRepository deviceInfoViewRepository;

    public BkavUserDto toDto(BkavUser bkavUser) {
        BkavUserDto dto = new BkavUserDto();
        dto.setId(String.valueOf(bkavUser.getId()));
        dto.setUsername(bkavUser.getUsername());
        dto.setName(bkavUser.getName());
        dto.setRole(bkavUser.getRole());
        dto.setGender(bkavUser.getGender());
        dto.setTotalDevice((int) deviceInfoViewRepository.countDeviceInfoViewByUsername(bkavUser.getUsername()));
        return dto;
    }
}
