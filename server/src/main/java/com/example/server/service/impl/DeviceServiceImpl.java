package com.example.server.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.dto.DeviceDto;
import com.example.server.model.response.PageView;
import com.example.server.model.response.SampleResponse;
import com.example.server.repository.dao.idao.BkavUserDao;
import com.example.server.repository.dao.idao.DeviceDao;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.service.iservice.DeviceService;
import com.example.server.utils.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
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
    public SampleResponse<DeviceDto> save(DeviceDto deviceDto) {

        if (!validateInputDevice(deviceDto)) {
            return new SampleResponse<>(null, Constants.ErrorMessage.INVALID_INPUT);
        }

        UUID id = deviceDto.getId();
        // case create
        if (id == null) {
            

            String name = deviceDto.getName();
            DeviceDto findDevice = deviceDao.findDeviceByName(name);
            if (findDevice != null) {
                return new SampleResponse<>(null, Constants.ErrorMessage.NAME_EXISTED);
            }
            deviceDto.setUserId(null);
            deviceDto.setStatus(Constants.Common.NUMBER_1_INT_NEGATIVE);

            return new SampleResponse<>(deviceDao.save(deviceDto));
        } else {
            DeviceDto foundDevice = deviceDao.findById(id).orElse(null);
            if (foundDevice == null) {
                return new SampleResponse<>(null, Constants.ErrorMessage.NOT_FOUND_DEVICE);
            }

            if (!foundDevice.getName().equals(deviceDto.getName())) {
                DeviceDto findExistDevice = deviceDao.findDeviceByName(deviceDto.getName());
                if (findExistDevice != null) {
                    return new SampleResponse<>(null, Constants.ErrorMessage.NAME_EXISTED);
                }
            }
            UUID userId = deviceDto.getUserId();
            if (userId != null) {
                BkavUserDto userDto = bkavUserDao.findById(userId).orElse(null);
                if (userDto == null) {
                    return new SampleResponse<>(null, Constants.ErrorMessage.NOT_FOUND_USER);
                }
                deviceDto.setStatus(Constants.Common.NUMBER_0_INT);
            } else {
                deviceDto.setStatus(Constants.Common.NUMBER_1_INT_NEGATIVE);
            }

            try {
                return new SampleResponse<>(deviceDao.save(deviceDto));
            } catch (Exception e) {
                return new SampleResponse<>(null, e.getMessage());
            }
        }

    }

    @Override
    public SampleResponse<Boolean> deleteById(UUID id) {

        try {
            DeviceDto findDevice = deviceDao.findById(id).orElse(null);
            if (findDevice == null) {
                return new SampleResponse<>(false, Constants.ErrorMessage.NOT_FOUND_DEVICE);
            }
            if (findDevice.getStatus() != Constants.Common.NUMBER_1_INT_NEGATIVE) {
                return new SampleResponse<>(false, Constants.ErrorMessage.DEVICE_ASSIGNED);
            }
            deviceDao.deleteById(id);
            return new SampleResponse<>(true);
        } catch (Exception e) {
            return new SampleResponse<>(false, e.getMessage());
        }
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
    public SampleResponse<Boolean> requestReturnDevice(String username, String id) {
        Boolean check = deviceDao.requestReturnDevice(username, id);
        if (!check) return new SampleResponse<>(false, Constants.ErrorMessage.REQUEST_RETURN_FAIL);
        return new SampleResponse<>(true);
    }

    @Override
    public SampleResponse<Boolean> acceptReturnDevice(String username, String id) {
        Boolean check = deviceDao.acceptReturnDevice(id);
        if (!check) return new SampleResponse<>(false, Constants.ErrorMessage.ACCEPT_RETURN_FAIL);
        return new SampleResponse<>(true);
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
