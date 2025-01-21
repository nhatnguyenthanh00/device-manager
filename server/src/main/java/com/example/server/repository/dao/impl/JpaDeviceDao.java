package com.example.server.repository.dao.impl;

import com.example.server.model.response.PageResponse;
import com.example.server.repository.dao.idao.DeviceDao;
import com.example.server.model.entity.Device;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.repository.view.DeviceInfoViewRepository;
import com.example.server.repository.DeviceRepository;
import com.example.server.utils.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class JpaDeviceDao implements DeviceDao {


    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    DeviceInfoViewRepository deviceInfoViewRepository;

    @Override
    public Optional<Device> getById(UUID id) {
        return deviceRepository.findById(id);
    }

    @Override
    public List<Device> getAll() {
        return deviceRepository.findAll();
    }

    @Override
    public Device save(Device device) {
        return deviceRepository.save(device);
    }

    @Override
    public void deleteById(UUID id) {
        deviceRepository.deleteById(id);
    }

    @Override
    public List<DeviceInfoView> getViewOfDevice(){
        return deviceInfoViewRepository.findAll();
    }

    @Override
    public PageResponse<DeviceInfoView> getAllDevicePaging(String search, String category, Integer status, int page) {
        Pageable pageable = PageRequest.of(page - Constants.Common.NUMBER_1_INT,Constants.Common.NUMBER_5_INT, Sort.by("name").ascending());
        Page<DeviceInfoView> pageDevice = Page.empty();

        if(category.isEmpty()){
            if(status == null) pageDevice = deviceInfoViewRepository.findDeviceInfoViewByNameContainingIgnoreCase(search, pageable);
            else{
                pageDevice = deviceInfoViewRepository.findDeviceInfoViewByNameContainingIgnoreCaseAndStatus(search,status,pageable);
            }
        }
        else{
            if(status == null) pageDevice = deviceInfoViewRepository.findDeviceInfoViewByNameContainingIgnoreCaseAndCategory(search,category,pageable);
            else{
                pageDevice = deviceInfoViewRepository.findDeviceInfoViewByNameContainingIgnoreCaseAndCategoryAndStatus(search,category,status,pageable);
            }
        }

        if(pageDevice.isEmpty()){
            return new PageResponse<>();
        }
        int totalItems = (int) pageDevice.getTotalElements();
        int totalPages = pageDevice.getTotalPages();
        List<DeviceInfoView> list = pageDevice.getContent();
        return new PageResponse<>(list,totalItems,totalPages);
    }

    @Override
    public Device findDeviceByName(String name){
        return deviceRepository.findByName(name);
    }

}
