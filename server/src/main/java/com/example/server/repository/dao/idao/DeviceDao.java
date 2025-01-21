package com.example.server.repository.dao.idao;

import com.example.server.model.response.PageResponse;
import com.example.server.repository.dao.EntityDao;
import com.example.server.model.entity.Device;
import com.example.server.model.entity.view.DeviceInfoView;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DeviceDao extends EntityDao<Device> {
    List<DeviceInfoView> getViewOfDevice();

    PageResponse<DeviceInfoView> getAllDevicePaging(String search, String category, Integer status, int page);

    Device findDeviceByName(String name);
}
