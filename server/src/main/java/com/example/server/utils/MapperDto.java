package com.example.server.utils;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.dto.DeviceDto;
import com.example.server.model.entity.BkavUser;
import com.example.server.model.entity.Device;
import com.example.server.repository.view.DeviceInfoViewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapperDto {
    @Autowired
    private DeviceInfoViewRepository deviceInfoViewRepository;

    public BkavUserDto toDto(BkavUser bkavUser) {
        if(bkavUser == null) return null;
        BkavUserDto dto = new BkavUserDto();
        dto.setUserId(bkavUser.getId());
        dto.setUsername(bkavUser.getUsername());
        dto.setPassword(bkavUser.getPassword());
        dto.setName(bkavUser.getName());
        dto.setRole(bkavUser.getRole());
        dto.setGender(bkavUser.getGender());
        dto.setTotalDevice((int) deviceInfoViewRepository.countDeviceInfoViewByUsername(bkavUser.getUsername()));
        return dto;
    }

    public BkavUser toBkavUser(BkavUserDto dto){
        if(dto == null) return null;
        BkavUser user = new BkavUser();
        if(dto.getUserId() != null ) user.setId(dto.getUserId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setName(dto.getName());
        user.setRole(dto.getRole());
        user.setGender(dto.getGender());
        return user;
    }

    public DeviceDto toDto(Device device){
        if(device == null) return null;
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setId(device.getId());
        deviceDto.setName(device.getName());
        deviceDto.setCategory(device.getCategory());
        deviceDto.setDescription(device.getDescription());
        deviceDto.setImage(device.getImage());
        deviceDto.setStatus(device.getStatus());
        deviceDto.setUserId(device.getBkavUserId());
        return deviceDto;
    }

    public Device toDevice(DeviceDto deviceDto){
        if(deviceDto == null) return null;
        Device device = new Device();
        device.setId(deviceDto.getId());
        device.setName(deviceDto.getName());
        device.setCategory(deviceDto.getCategory());
        device.setDescription(deviceDto.getDescription());
        device.setImage(deviceDto.getImage());
        device.setStatus(deviceDto.getStatus());
        device.setBkavUserId(deviceDto.getUserId());
        return device;
    }
}
