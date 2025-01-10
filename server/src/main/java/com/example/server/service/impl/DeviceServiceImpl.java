package com.example.server.service.impl;

import com.example.server.dao.JpaDeviceDao;
import com.example.server.model.BkavUser;
import com.example.server.model.Device;
import com.example.server.model.view.DeviceInfoView;
import com.example.server.repository.view.DeviceInfoViewRepository;
import com.example.server.repository.UserRepository;
import com.example.server.repository.DeviceRepository;
import com.example.server.resquest.UpdateDeviceRequest;
import com.example.server.service.iService.IDeviceServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeviceServiceImpl implements IDeviceServiceService {

    @Autowired
    JpaDeviceDao jpaDeviceDao;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    DeviceInfoViewRepository deviceInfoViewRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    public Device save(Device device){
        return jpaDeviceDao.save(device);
    }

    @Override
    public void delete(Device device) {
        if(device.getId() != null) jpaDeviceDao.deleteById(device.getId());
        return;
    }

    @Override
    public List<DeviceInfoView> getAllDevice(){
        return jpaDeviceDao.getViewOfDevice();
    }

    @Override
    public boolean updateDevice(UpdateDeviceRequest request) {
        UUID deviceId = request.getDeviceId();
        UUID userId = request.getUserId();
        if (!deviceRepository.existsById(deviceId)) {
            return false;
        }
        Device foundDevice = deviceRepository.findById(deviceId).orElse(null);
        if (foundDevice != null){
            foundDevice.setName(request.getName());
            foundDevice.setCategory(request.getCategory());
            foundDevice.setDescription(request.getDescription());
            foundDevice.setImage(request.getImage());
            if (userId != null){
                if(!userRepository.existsById(userId)){
                    return false;
                }
                BkavUser findBkavUser = userRepository.findById(userId).orElse(null);
                if(findBkavUser !=null) foundDevice.setBkavUser(findBkavUser);
                if(findBkavUser == null) return false;
            }
            deviceRepository.save(foundDevice);
            return true;
        }

        return false;
    }
}
