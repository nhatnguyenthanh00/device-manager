package com.example.server.service.iservice;

import com.example.server.model.dto.DeviceDto;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.model.response.PageView;
import com.example.server.model.response.SampleResponse;
import com.example.server.service.EntityService;

public interface DeviceService extends EntityService<DeviceDto> {

    PageView<DeviceInfoView> findAllDevice(String search, String category, Integer status, int page);
    PageView<DeviceInfoView> findDeviceByUsername(String username, String search, String category, Integer status, int page);



    SampleResponse<Boolean> requestReturnDevice(String username, String id);
    SampleResponse<Boolean> acceptReturnDevice(String username, String id);
}
