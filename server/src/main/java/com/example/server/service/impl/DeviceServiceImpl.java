package com.example.server.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.dto.DeviceDto;
import com.example.server.model.response.ErrorResponse;
import com.example.server.model.response.PageView;
import com.example.server.repository.dao.idao.BkavUserDao;
import com.example.server.repository.dao.idao.DeviceDao;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.service.iservice.DeviceService;
import com.example.server.utils.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> save(DeviceDto deviceDto) {

        if (!validateInputDevice(deviceDto)) {
            return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.INVALID_INPUT),HttpStatus.BAD_REQUEST);
//            return new SampleResponse<>(null, Constants.ErrorMessage.INVALID_INPUT);
        }

        UUID id = deviceDto.getId();
        // case create
        if (id == null) {


            String name = deviceDto.getName();
            DeviceDto findDevice = deviceDao.findDeviceByName(name);
            if (findDevice != null) {
                return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.NAME_EXISTED),HttpStatus.BAD_REQUEST);
//                return new SampleResponse<>(null, Constants.ErrorMessage.NAME_EXISTED);
            }
            deviceDto.setUserId(null);
            deviceDto.setStatus(Constants.Common.NUMBER_1_INT_NEGATIVE);
            return new ResponseEntity<>(deviceDao.save(deviceDto),HttpStatus.OK);
//            return new SampleResponse<>(deviceDao.save(deviceDto));
        } else {
            DeviceDto foundDevice = deviceDao.findById(id).orElse(null);
            if (foundDevice == null) {
                return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.NOT_FOUND_DEVICE),HttpStatus.BAD_REQUEST);
//                return new SampleResponse<>(null, Constants.ErrorMessage.NOT_FOUND_DEVICE);
            }

            if (!foundDevice.getName().equals(deviceDto.getName())) {
                DeviceDto findExistDevice = deviceDao.findDeviceByName(deviceDto.getName());
                if (findExistDevice != null) {
                    return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.NAME_EXISTED),HttpStatus.BAD_REQUEST);
//                    return new SampleResponse<>(null, Constants.ErrorMessage.NAME_EXISTED);
                }
            }
            UUID userId = deviceDto.getUserId();
            if (userId != null) {
                BkavUserDto userDto = bkavUserDao.findById(userId).orElse(null);
                if (userDto == null) {
                    return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.NOT_FOUND_USER),HttpStatus.BAD_REQUEST);
//                    return new SampleResponse<>(null, Constants.ErrorMessage.NOT_FOUND_USER);
                }
                deviceDto.setStatus(Constants.Common.NUMBER_0_INT);
            } else {
                deviceDto.setStatus(Constants.Common.NUMBER_1_INT_NEGATIVE);
            }

//            try {
//                return new SampleResponse<>(deviceDao.save(deviceDto));
//            } catch (Exception e) {
//                return new SampleResponse<>(null, e.getMessage());
//            }
            return new ResponseEntity<>(deviceDao.save(deviceDto),HttpStatus.OK);
        }

    }

    @Override
    public ResponseEntity<?> deleteById(UUID id) {

        DeviceDto findDevice = deviceDao.findById(id).orElse(null);
        if (findDevice == null) {
            return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.NOT_FOUND_DEVICE), HttpStatus.BAD_REQUEST);
//                return new SampleResponse<>(false, Constants.ErrorMessage.NOT_FOUND_DEVICE);
        }
        if (findDevice.getStatus() != Constants.Common.NUMBER_1_INT_NEGATIVE) {
            return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.DEVICE_ASSIGNED), HttpStatus.BAD_REQUEST);
//                return new SampleResponse<>(false, Constants.ErrorMessage.DEVICE_ASSIGNED);
        }
        deviceDao.deleteById(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
//            return new SampleResponse<>(true);
    }

    @Override
    public PageView<DeviceInfoView> findAllDevice(String search, String category, Integer status, int page) {
        return deviceDao.findAllDevicePaging(search, category, status, page);
    }

    @Override
    public PageView<DeviceInfoView> findDeviceByUsername(String username, String search, String category, Integer status, int page) {
        return deviceDao.findAllDevicePagingByUsername(username, search, category, status, page);
    }

    @Override
    public ResponseEntity<?> requestReturnDevice(String username, String id) {
        Boolean check = deviceDao.requestReturnDevice(username, id);
        if (!check) return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.REQUEST_RETURN_FAIL),HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> acceptReturnDevice(String username, String id) {
        Boolean check = deviceDao.responseReturnDevice(id,true);
        if (!check) return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.ACCEPT_RETURN_FAIL),HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> refuseReturnDevice(String username, String id) {
        Boolean check = deviceDao.responseReturnDevice(id,false);
        if (!check) return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.REFUSE_RETURN_FAIL),HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }


    private static boolean validateInputDevice(DeviceDto deviceDto) {
        if (StringUtil.isNullOrEmpty(deviceDto.getName())
                || deviceDto.getCategory() == null) return false;
        String nameRegex = "^[a-zA-Z0-9_]{5,}$";
        if (!Pattern.matches(nameRegex, deviceDto.getName())) {
            return false;
        }
        return true;
    }
}
