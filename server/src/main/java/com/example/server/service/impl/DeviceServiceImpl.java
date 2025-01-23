package com.example.server.service.impl;

import com.example.server.config.security.UserPrincipal;
import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.dto.DeviceDto;
import com.example.server.model.response.PageResponse;
import com.example.server.model.response.SampleResponse;
import com.example.server.model.resquest.ActionByIdRequest;
import com.example.server.repository.dao.idao.BkavUserDao;
import com.example.server.repository.dao.idao.DeviceDao;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.model.resquest.UpdateDeviceRequest;
import com.example.server.service.iservice.DeviceService;
import com.example.server.utils.enums.DeviceCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class DeviceServiceImpl implements DeviceService {


    @Autowired
    DeviceDao deviceDao;

    @Autowired
    BkavUserDao bkavUserDao;

    @Override
    public DeviceDto save(DeviceDto deviceDto) throws Exception {

        UUID id = deviceDto.getId();
        // case create
        if(id == null){

            if(!validateInputCreateDevice(deviceDto)) throw new Exception("Invalid input");

            String name = deviceDto.getName();
            DeviceDto findDevice = deviceDao.findDeviceByName(name);
            if(findDevice != null) throw new Exception("Name already existed");
            String description = deviceDto.getDescription();
            String image = deviceDto.getImage();
            DeviceCategory category = deviceDto.getCategory();

            DeviceDto newDevice = new DeviceDto();
            newDevice.setName(name);
            newDevice.setDescription(description);
            newDevice.setCategory(category);
            newDevice.setImage(image);
            newDevice.setStatus(-1);

            return deviceDao.save(newDevice);
        }
        else{
            return deviceDao.save(deviceDto);
        }

//        DeviceDto findDevice = deviceDao.findDeviceByName(deviceDto.getName());
//        if (findDevice != null) return null;
//
//        return deviceDao.save(deviceDto);
    }

    @Override
    public boolean deleteById(UUID id) {
        DeviceDto findDevice = deviceDao.getById(id).orElse(null);
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
    public PageResponse<DeviceInfoView> getDeviceByUsername(String username, String search, String category, String status, int page){
        Integer statusNum = null ;
        try{
            statusNum = Integer.parseInt(status);
        } catch (Exception ignored){
        }
        return deviceDao.getAllDevicePagingByUsername(username,search, category, statusNum, page);
    }



    @Override
    public SampleResponse<Boolean> updateDevice(UpdateDeviceRequest request) {
        String username = request.getUserName();
        UUID deviceId = UUID.fromString(request.getDeviceId());

        DeviceDto foundDevice = deviceDao.getById(deviceId).orElse(null);

        if (foundDevice == null) {
            return new SampleResponse<>(false,"Not found device");
        }

        if(!foundDevice.getName().equals(request.getName())){
            DeviceDto findExistDevice = deviceDao.findDeviceByName(request.getName());
            if(findExistDevice != null){
                return new SampleResponse<>(false,"Device name existed");
            }
        }


        if (!isNullOrEmpty(username)) {
            BkavUserDto bkavUser = bkavUserDao.findByUserName(username);
            if(bkavUser == null){
                return new SampleResponse<>(false,"Not found user "+username);
            }
            foundDevice.setStatus(0);
            foundDevice.setUserId(bkavUser.getUserId());
        }

        else{
            foundDevice.setStatus(-1);
            foundDevice.setUserId(null);
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
    public SampleResponse<Boolean> deleteDeviceById(String idRequest) {
        try{
            UUID id = UUID.fromString(idRequest);
            boolean check = deleteById(id);
            if (check){
                return new SampleResponse<>( true);
            }
            return new SampleResponse<>(false,"Delete device fail");
        } catch (Exception e){
            return new SampleResponse<>(false,"Delete device fail");
        }
    }

    @Override
    public SampleResponse<Boolean> requestReturnDevice(ActionByIdRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserPrincipal userPrincipal = (UserPrincipal)principal;
        Boolean check = deviceDao.requestReturnDevice(userPrincipal.getUsername(),request.getId());
        if(!check) return new SampleResponse<>(false,"Request return fail");
        return new SampleResponse<>(true);
    }

    @Override
    public SampleResponse<Boolean> acceptReturnDevice(ActionByIdRequest request) {
        Boolean check = deviceDao.acceptReturnDevice(request.getId());
        if(!check) return new SampleResponse<>(false,"Accept return fail");
        return new SampleResponse<>(true);
    }

    private static boolean isNullOrEmpty(String str){
        if(str == null) return true;
        if(str.trim().isEmpty()) return true;
        return false;
    }

    private static boolean validateInputCreateDevice(DeviceDto deviceDto){
        if(isNullOrEmpty(deviceDto.getName())
                || deviceDto.getCategory() == null) return false;
        String nameRegex = "^[a-zA-Z0-9]{6,}$";
        if (!Pattern.matches(nameRegex, deviceDto.getName())) {
            return false;
        }
        return true;
    }
}
