package com.example.server.service;

import com.example.server.model.Account;
import com.example.server.model.Device;
import com.example.server.repository.AccountRepository;
import com.example.server.repository.DeviceRepository;
import com.example.server.resquest.UpdateDeviceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceService {
    @Autowired
    DeviceRepository deviceRepository;
    @Autowired
    AccountRepository accountRepository;

    public Device saveDevice(Device device){
        return deviceRepository.save(device);
    }

    public List<Device> getAllDevice(){
        return deviceRepository.findAll();
    }

    public boolean updateDevice(UpdateDeviceRequest request) {
        Integer deviceId = request.getDeviceId();
        Integer accountId = request.getAccountId();
        if (!deviceRepository.existsById(deviceId)) {
            return false;
        }
        Device foundDevice = deviceRepository.findById(deviceId).orElse(null);
        if (foundDevice != null){
            foundDevice.setName(request.getName());
            foundDevice.setCategory(request.getCategory());
            foundDevice.setDescription(request.getDescription());
            foundDevice.setImage(request.getImage());
            if (accountId != null){
                if(!accountRepository.existsById(accountId)){
                    return false;
                }
                Account findAccount = accountRepository.findById(accountId).orElse(null);
                if(findAccount!=null) foundDevice.setAccount(findAccount);
            }
        }

        deviceRepository.save(foundDevice);

        return true; // Cập nhật thiết bị
    }
}
