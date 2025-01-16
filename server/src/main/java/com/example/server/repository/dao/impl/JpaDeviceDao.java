package com.example.server.repository.dao.impl;

import com.example.server.repository.dao.idao.DeviceDao;
import com.example.server.model.entity.Device;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.repository.view.DeviceInfoViewRepository;
import com.example.server.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaDeviceDao implements DeviceDao {


    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    DeviceInfoViewRepository deviceInfoViewRepository;

    @Override
    public Optional<Device> getById(UUID id) {
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

    @Override
    public List<DeviceInfoView> getViewOfDevice(){
        return deviceInfoViewRepository.findAll();
    }

}
