package com.example.server.service.iservice;

import com.example.server.model.entity.Device;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.model.response.PageResponse;
import com.example.server.model.response.SampleResponse;
import com.example.server.model.resquest.CreateNewDeviceRequest;
import com.example.server.model.resquest.ActionByIdRequest;
import com.example.server.model.resquest.UpdateDeviceRequest;
import com.example.server.service.EntityService;

public interface DeviceService extends EntityService<Device> {

    PageResponse<DeviceInfoView>getAllDevice(String search, String category, String status, int page);
    PageResponse<DeviceInfoView>getDeviceByUsername(String username, String search, String category, String status, int page);

    SampleResponse<Boolean> updateDevice(UpdateDeviceRequest request);

    SampleResponse<Boolean> saveNewDevice(CreateNewDeviceRequest request);

    SampleResponse<Boolean> deleteDeviceById(ActionByIdRequest request);

    SampleResponse<Boolean> requestReturnDevice(ActionByIdRequest request);
    SampleResponse<Boolean> acceptReturnDevice(ActionByIdRequest request);
}
