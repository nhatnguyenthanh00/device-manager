package com.example.server.repository.dao.idao;

import com.example.server.model.dto.DeviceDto;
import com.example.server.model.response.PageResponse;
import com.example.server.repository.dao.EntityDao;
import com.example.server.model.entity.Device;
import com.example.server.model.entity.view.DeviceInfoView;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DeviceDao extends EntityDao<DeviceDto> {
    
    PageResponse<DeviceInfoView> getAllDevicePaging(String search, String category, Integer status, int page);
    PageResponse<DeviceInfoView> getAllDevicePagingByUsername(String username, String search, String category, Integer status, int page);

    PageResponse<DeviceInfoView> getAllDeviceByUsername(String username, Integer page);

    DeviceDto findDeviceByName(String name);

    Boolean requestReturnDevice(String username,String deviceId);
    Boolean acceptReturnDevice(String deviceId);
}
