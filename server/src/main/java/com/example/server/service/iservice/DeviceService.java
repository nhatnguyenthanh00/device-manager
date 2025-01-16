package com.example.server.service.iservice;

import com.example.server.model.entity.Device;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.model.resquest.UpdateDeviceRequest;
import com.example.server.service.EntityService;

import java.util.List;

public interface DeviceService extends EntityService<Device> {

    List<DeviceInfoView> getAllDevice();

    boolean updateDevice(UpdateDeviceRequest request);
}
