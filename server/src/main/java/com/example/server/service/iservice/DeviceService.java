package com.example.server.service.iservice;

import com.example.server.model.dto.DeviceDto;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.model.response.PageView;
import com.example.server.service.EntityService;
import org.springframework.http.ResponseEntity;

public interface DeviceService extends EntityService<DeviceDto> {

    PageView<DeviceInfoView> findAllDevice(String search, String category, Integer status, int page);
    PageView<DeviceInfoView> findDeviceByUsername(String username, String search, String category, Integer status, int page);



    ResponseEntity<?> requestReturnDevice(String username, String id);
    ResponseEntity<?> acceptReturnDevice(String username, String id);
    ResponseEntity<?> refuseReturnDevice(String username, String id);
}
