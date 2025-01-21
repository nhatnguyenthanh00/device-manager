package com.example.server.service.impl;

import com.example.server.model.response.PageResponse;
import com.example.server.model.response.SampleResponse;
import com.example.server.model.resquest.CreateNewDeviceRequest;
import com.example.server.model.resquest.DeleteByIdRequest;
import com.example.server.repository.dao.idao.BkavUserDao;
import com.example.server.repository.dao.idao.DeviceDao;
import com.example.server.model.entity.BkavUser;
import com.example.server.model.entity.Device;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.model.resquest.UpdateDeviceRequest;
import com.example.server.service.iservice.DeviceService;
import com.example.server.utils.constants.Constants;
import com.example.server.utils.enums.DeviceCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DeviceServiceImpl implements DeviceService {


    @Autowired
    DeviceDao deviceDao;

    @Autowired
    BkavUserDao bkavUserDao;

    @Override
    public Device save(Device device) {

        Device findDevice = deviceDao.findDeviceByName(device.getName());
        if (findDevice != null) return null;

        return deviceDao.save(device);
    }

    @Override
    public boolean deleteById(UUID id) {
        Device findDevice = deviceDao.getById(id).orElse(null);
        if(findDevice == null){
            return false;
        }
        if(findDevice.getStatus() != -1){
            return false;
        }
        try{
            deviceDao.deleteById(id);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public PageResponse<DeviceInfoView> getAllDevice(String search, String category, String status, int page) {
        Integer statusNum = null ;
        try{
            statusNum = Integer.parseInt(status);
        } catch (Exception ignored){
        }
        return deviceDao.getAllDevicePaging(search, category, statusNum, page);
    }

    @Override
    public SampleResponse<Boolean> updateDevice(UpdateDeviceRequest request) {
        String username = request.getUserName();
        UUID deviceId = UUID.fromString(request.getDeviceId());

        Device foundDevice = deviceDao.getById(deviceId).orElse(null);

        if (foundDevice == null) {
            return new SampleResponse<>(false,"Not found device");
        }

        if(!foundDevice.getName().equals(request.getName())){
            Device foundByNameDevice = deviceDao.findDeviceByName(request.getName());
            if(foundByNameDevice != null){
                return new SampleResponse<>(false,"Device name existed");
            }
        }


        if (!username.isEmpty()) {
            BkavUser bkavUser = bkavUserDao.findByUserName(username);
            if(bkavUser == null){
                return new SampleResponse<>(false,"Not found user "+username);
            }
            foundDevice.setStatus(0);
            foundDevice.setBkavUserId(bkavUser.getId());
        }

        else{
            foundDevice.setStatus(-1);
            foundDevice.setBkavUserId(null);
        }

        foundDevice.setName(request.getName());
        foundDevice.setCategory(request.getCategory());
        foundDevice.setDescription(request.getDescription());
        foundDevice.setImage(request.getImage());
        try{
            deviceDao.save(foundDevice);
            return new SampleResponse<>(true);
        } catch (Exception e){
            return new SampleResponse<>(false,"Internal server error");
        }
    }

    @Override
    public SampleResponse<Boolean> saveNewDevice(CreateNewDeviceRequest request) {
        if(!Constants.Common.ALL_CATEGORY.contains(request.getCategory())){
            return new SampleResponse<>(false,"Invalid category");
        }

        Device newDevice = new Device();
        newDevice.setName(request.getName());
        newDevice.setDescription(request.getDescription());
        if(request.getCategory().equals("PC")) newDevice.setCategory(DeviceCategory.PC);
        if(request.getCategory().equals("LAPTOP")) newDevice.setCategory(DeviceCategory.LAPTOP);
        if(request.getCategory().equals("MOUSE")) newDevice.setCategory(DeviceCategory.MOUSE);
        if(request.getCategory().equals("PHONE")) newDevice.setCategory(DeviceCategory.PHONE);
        newDevice.setImage(request.getImage());
        newDevice.setStatus(-1);
        Device saveDevice = save(newDevice);
        if(saveDevice == null){
            return new SampleResponse<>(false,"Name already exists");
        }
        return new SampleResponse<>(true);
    }

    @Override
    public SampleResponse<Boolean> deleteDeviceById(DeleteByIdRequest request) {
        try{
            UUID id = UUID.fromString(request.getId());
            boolean check = deleteById(id);
            if (check){
                return new SampleResponse<>( true);
            }
            return new SampleResponse<>(false,"Delete device fail");
        } catch (Exception e){
            return new SampleResponse<>(false,"Delete device fail");
        }
    }
}
