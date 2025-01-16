package com.example.server.repository.dao.idao;

import com.example.server.repository.dao.EntityDao;
import com.example.server.model.entity.Device;
import com.example.server.model.entity.view.DeviceInfoView;

import java.util.List;

public interface DeviceDao extends EntityDao<Device> {
    List<DeviceInfoView> getViewOfDevice();
}
