package com.example.server.repository.dao.impl;

import com.example.server.model.dto.DeviceDto;
import com.example.server.model.entity.BkavUser;
import com.example.server.model.response.PageView;
import com.example.server.repository.BkavUserRepository;
import com.example.server.repository.dao.idao.DeviceDao;
import com.example.server.model.entity.Device;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.repository.view.DeviceInfoViewRepository;
import com.example.server.repository.DeviceRepository;
import com.example.server.utils.MapperDto;
import com.example.server.utils.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JpaDeviceDao implements DeviceDao {


    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    BkavUserRepository bkavUserRepository;

    @Autowired
    DeviceInfoViewRepository deviceInfoViewRepository;

    @Autowired
    MapperDto mapperDto;

    @Override
    public Optional<DeviceDto> findById(UUID id) {
        return Optional.ofNullable(mapperDto.toDto(deviceRepository.findById(id).orElse(null)));
    }

    @Override
    public List<DeviceDto> findAll() {
        List<Device> devices = deviceRepository.findAll();
        return devices.stream()
                .map(mapperDto::toDto).collect(Collectors.toList());
    }

    @Override
    public DeviceDto save(DeviceDto deviceDto) {
        Device saveDevice = mapperDto.toDevice(deviceDto);
        Device device = deviceRepository.save(saveDevice);
        return mapperDto.toDto(device);
    }

    @Override
    public void deleteById(UUID id) {
        deviceRepository.deleteById(id);
    }


    @Override
    public PageView<DeviceInfoView> findAllDevicePaging(String search, String category, Integer status, int page) {
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
            return new PageView<>();
        }
        int totalItems = (int) pageDevice.getTotalElements();
        int totalPages = pageDevice.getTotalPages();
        List<DeviceInfoView> list = pageDevice.getContent();
        return new PageView<>(list,totalItems,totalPages);
    }

    @Override
    public PageView<DeviceInfoView> findAllDevicePagingByUsername(String username, String search, String category, Integer status, int page) {
        Pageable pageable = PageRequest.of(page - Constants.Common.NUMBER_1_INT,Constants.Common.NUMBER_5_INT, Sort.by("name").ascending());
        Page<DeviceInfoView> pageDevice = Page.empty();

        if(category.isEmpty()){
            if(status == null) pageDevice = deviceInfoViewRepository.findDeviceInfoViewByUsernameAndNameContainingIgnoreCase(username,search, pageable);
            else{
                pageDevice = deviceInfoViewRepository.findDeviceInfoViewByUsernameAndNameContainingIgnoreCaseAndStatus(username,search,status,pageable);
            }
        }
        else{
            if(status == null) pageDevice = deviceInfoViewRepository.findDeviceInfoViewByUsernameAndNameContainingIgnoreCaseAndCategory(username,search,category,pageable);
            else{
                pageDevice = deviceInfoViewRepository.findDeviceInfoViewByUsernameAndNameContainingIgnoreCaseAndCategoryAndStatus(username,search,category,status,pageable);
            }
        }

        if(pageDevice.isEmpty()){
            return new PageView<>();
        }
        int totalItems = (int) pageDevice.getTotalElements();
        int totalPages = pageDevice.getTotalPages();
        List<DeviceInfoView> list = pageDevice.getContent();
        return new PageView<>(list,totalItems,totalPages);
    }

    @Override
    public PageView<DeviceInfoView> findAllDeviceByUsername(String username, Integer page) {
        Pageable pageable = PageRequest.of(page-Constants.Common.NUMBER_1_INT,Constants.Common.NUMBER_5_INT, Sort.by("name").ascending());
        Page<DeviceInfoView> infoViewPage = deviceInfoViewRepository.findDeviceInfoViewByUsername(username,pageable);
        if(infoViewPage.isEmpty()) return new PageView<>();
        int totalItems = (int) infoViewPage.getTotalElements();
        int totalPages = infoViewPage.getTotalPages();
        List<DeviceInfoView> list = infoViewPage.getContent();
        return new PageView<>(list,totalItems,totalPages);
    }

    @Override
    public DeviceDto findDeviceByName(String name){
        return mapperDto.toDto(deviceRepository.findByName(name));
    }

    @Override
    public Boolean requestReturnDevice(String username,String deviceId) {
        UUID id = UUID.fromString(deviceId);
        Device device = deviceRepository.findById(id).orElse(null);
        if(device == null) return false;
        if(device.getBkavUserId() == null) return false;
        BkavUser user = bkavUserRepository.findByUsername(username);
        if(!user.getId().equals(device.getBkavUserId())) throw new AccessDeniedException("User don't have access this function");
        if(device.getStatus() != Constants.Common.NUMBER_0_INT) return false;
        device.setStatus(Constants.Common.NUMBER_1_INT);
        try{
            deviceRepository.save(device);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public Boolean responseReturnDevice(String deviceId, boolean accept) {
        UUID id = UUID.fromString(deviceId);
        Device device = deviceRepository.findById(id).orElse(null);
        if(device == null) return false;
        if(device.getBkavUserId() == null) return false;
        if(device.getStatus() != Constants.Common.NUMBER_1_INT) return false;
        if(accept){
            device.setStatus(Constants.Common.NUMBER_1_INT_NEGATIVE);
            device.setBkavUserId(null);
        }
        else device.setStatus(Constants.Common.NUMBER_0_INT);
        try{
            deviceRepository.save(device);
            return true;
        } catch (Exception e){
            return false;
        }
    }


}
