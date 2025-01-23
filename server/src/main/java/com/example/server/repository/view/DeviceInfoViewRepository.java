package com.example.server.repository.view;

import com.example.server.model.entity.view.DeviceInfoView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;
public interface DeviceInfoViewRepository extends JpaRepository<DeviceInfoView, UUID> {

    Page<DeviceInfoView> findDeviceInfoViewByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<DeviceInfoView> findDeviceInfoViewByNameContainingIgnoreCaseAndCategory(String name, String category, Pageable pageable);

    Page<DeviceInfoView> findDeviceInfoViewByNameContainingIgnoreCaseAndCategoryAndStatus(String name, String category, int status, Pageable pageable);

    Page<DeviceInfoView> findDeviceInfoViewByNameContainingIgnoreCaseAndStatus(String name, int status, Pageable pageable);

    Page<DeviceInfoView> findDeviceInfoViewByUsernameAndNameContainingIgnoreCase(String username, String name, Pageable pageable);

    Page<DeviceInfoView> findDeviceInfoViewByUsernameAndNameContainingIgnoreCaseAndCategory(String username, String name, String category, Pageable pageable);

    Page<DeviceInfoView> findDeviceInfoViewByUsernameAndNameContainingIgnoreCaseAndCategoryAndStatus(String username, String name, String category, int status, Pageable pageable);

    Page<DeviceInfoView> findDeviceInfoViewByUsernameAndNameContainingIgnoreCaseAndStatus(String username, String name, int status, Pageable pageable);

    Page<DeviceInfoView> findDeviceInfoViewByUsername(String username, Pageable pageable);

    long countDeviceInfoViewByUsername(String username);
}
