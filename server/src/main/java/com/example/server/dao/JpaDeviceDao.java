package com.example.server.dao;

import com.example.server.model.Device;
import com.example.server.model.view.DeviceInfoView;
import com.example.server.repository.view.DeviceInfoViewRepository;
import com.example.server.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaDeviceDao implements EntityDao<Device> {


    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    DeviceInfoViewRepository deviceInfoViewRepository;

    @Override
    public Optional<Device> get(UUID id) {
        return deviceRepository.findById(id);
    }

    @Override
    public List<Device> getAll() {
        return deviceRepository.findAll();
    }

    @Override
    public Device save(Device device) {
        return deviceRepository.save(device);
    }

    @Override
    public void deleteById(UUID id) {
        deviceRepository.deleteById(id);
    }

    public List<DeviceInfoView> getViewOfDevice(){
        return deviceInfoViewRepository.findAll();
    }

}
