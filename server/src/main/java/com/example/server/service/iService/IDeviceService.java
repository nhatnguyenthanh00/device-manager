package com.example.server.service.iService;

import com.example.server.model.Device;
import com.example.server.model.view.DeviceInfoView;
import com.example.server.resquest.UpdateDeviceRequest;

import java.util.List;

public interface IDeviceService {

    List<DeviceInfoView> getAllDevice();

    boolean updateDevice(UpdateDeviceRequest request);
}
