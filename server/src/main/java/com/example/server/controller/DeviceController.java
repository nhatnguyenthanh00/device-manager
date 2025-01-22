package com.example.server.controller;

import com.example.server.config.security.UserPrincipal;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.model.response.PageResponse;
import com.example.server.model.response.SampleResponse;
import com.example.server.model.resquest.CreateNewDeviceRequest;
import com.example.server.model.resquest.ActionByIdRequest;
import com.example.server.model.resquest.UpdateDeviceRequest;
import com.example.server.service.impl.DeviceServiceImpl;
import com.example.server.utils.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.ApiEndpoint.COMMON_BASE_PATH)
public class DeviceController {

    @Autowired
    DeviceServiceImpl deviceService;
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/device")
    public SampleResponse<PageResponse<DeviceInfoView>> getAllDevice(@RequestParam(required = false, defaultValue = "") String category,
                                                                     @RequestParam(required = false, defaultValue = "") String status,
                                                                     @RequestParam(required = false, defaultValue = "") String search,
                                                                     @RequestParam(required = false, defaultValue = "1") Integer page){
        PageResponse<DeviceInfoView> data = deviceService.getAllDevice(search,category,status,page);
        return new SampleResponse<>(data);
    }

    @GetMapping("/my-device")
    public SampleResponse<PageResponse<DeviceInfoView>> getMyDevice(@RequestParam(required = false, defaultValue = "") String category,
                                                                     @RequestParam(required = false, defaultValue = "") String status,
                                                                     @RequestParam(required = false, defaultValue = "") String search,
                                                                     @RequestParam(required = false, defaultValue = "1") Integer page){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserPrincipal userPrincipal = (UserPrincipal)principal;
        PageResponse<DeviceInfoView> data = deviceService.getDeviceByUsername(userPrincipal.getUsername(),search,category,status,page);
        return new SampleResponse<>(data);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/device")
    public SampleResponse<Boolean> createDevice(@RequestBody CreateNewDeviceRequest request) {
        return deviceService.saveNewDevice(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/device")
    public SampleResponse<Boolean> updateDevice(@RequestBody UpdateDeviceRequest request) {
        return deviceService.updateDevice(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/device")
    public SampleResponse<Boolean> deleteDevice(@RequestBody ActionByIdRequest request){
        return deviceService.deleteDeviceById(request);
    }

    @PostMapping("/device-return")
    public SampleResponse<Boolean> returnDevice(@RequestBody ActionByIdRequest request){
        return deviceService.requestReturnDevice(request);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/accept-return")
    public SampleResponse<Boolean> acceptReturnDevice(@RequestBody ActionByIdRequest request){
        return deviceService.acceptReturnDevice(request);
    }
}
