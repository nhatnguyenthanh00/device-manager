package com.example.server.service.impl;

import com.example.server.repository.dao.impl.JpaDeviceDao;
import com.example.server.model.entity.BkavUser;
import com.example.server.model.entity.Device;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.repository.view.DeviceInfoViewRepository;
import com.example.server.repository.BkavUserRepository;
import com.example.server.repository.DeviceRepository;
import com.example.server.model.resquest.UpdateDeviceRequest;
import com.example.server.service.iservice.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeviceServiceImpl implements DeviceService {

    @Autowired
    JpaDeviceDao jpaDeviceDao;

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    DeviceInfoViewRepository deviceInfoViewRepository;

    @Autowired
    BkavUserRepository bkavUserRepository;

    @Override
    public Device save(Device device){
        return jpaDeviceDao.save(device);
    }

    @Override
    public boolean deleteById(UUID id) {
        return false;
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
                if(!bkavUserRepository.existsById(userId)){
                    return false;
                }
                BkavUser findBkavUser = bkavUserRepository.findById(userId).orElse(null);
                if(findBkavUser !=null) foundDevice.setBkavUser(findBkavUser);
                if(findBkavUser == null) return false;
            }
            deviceRepository.save(foundDevice);
            return true;
        }

        return false;
    }
}
