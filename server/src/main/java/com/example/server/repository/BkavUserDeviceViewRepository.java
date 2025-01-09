package com.example.server.repository;

import com.example.server.model.view.BkavUserDeviceView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface BkavUserDeviceViewRepository extends JpaRepository<BkavUserDeviceView, UUID> {
    @Query(value = "SELECT * FROM bkav_user_device_view", nativeQuery = true)
    List<BkavUserDeviceView> findAllBkavUser();
}
