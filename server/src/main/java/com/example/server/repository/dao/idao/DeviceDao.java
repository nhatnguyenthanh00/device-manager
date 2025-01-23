package com.example.server.repository.dao.idao;

import com.example.server.model.dto.DeviceDto;
import com.example.server.model.response.PageView;
import com.example.server.repository.dao.EntityDao;
import com.example.server.model.entity.view.DeviceInfoView;

public interface DeviceDao extends EntityDao<DeviceDto> {

    PageView<DeviceInfoView> findAllDevicePaging(String search, String category, Integer status, int page);
    PageView<DeviceInfoView> findAllDevicePagingByUsername(String username, String search, String category, Integer status, int page);

    PageView<DeviceInfoView> findAllDeviceByUsername(String username, Integer page);

    DeviceDto findDeviceByName(String name);

    Boolean requestReturnDevice(String username,String deviceId);
    Boolean acceptReturnDevice(String deviceId);
}
