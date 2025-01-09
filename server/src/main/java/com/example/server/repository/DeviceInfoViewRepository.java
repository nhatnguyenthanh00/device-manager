package com.example.server.repository;

import com.example.server.model.view.DeviceInfoView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;
public interface DeviceInfoViewRepository extends JpaRepository<DeviceInfoView, UUID> {
    @Query(value = "SELECT * FROM device_info_view", nativeQuery = true)
    List<DeviceInfoView> findAllDeviceInfo();
}
