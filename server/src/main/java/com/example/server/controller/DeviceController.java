package com.example.server.controller;

import com.example.server.config.security.UserPrincipal;
import com.example.server.model.dto.DeviceDto;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.model.response.PageResponse;
import com.example.server.model.response.SampleResponse;
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

    /**
     * @description Get all devices with filtering options
     * @endpoint  GET /api/admin/device
     * @param    category Category to filter by (default "")
     * @param    status Status to filter by (default "")
     * @param    search Name or keyword to search (default "")
     * @param    page Page number (default 1)
     * @return   PageResponse<DeviceInfoView> containing device information
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin/device")
    public SampleResponse<PageResponse<DeviceInfoView>> getAllDevice(@RequestParam(required = false, defaultValue = "") String category,
                                                                     @RequestParam(required = false, defaultValue = "") String status,
                                                                     @RequestParam(required = false, defaultValue = "") String search,
                                                                     @RequestParam(required = false, defaultValue = "1") Integer page){
        PageResponse<DeviceInfoView> data = deviceService.getAllDevice(search,category,status,page);
        return new SampleResponse<>(data);
    }

    /**
     * @description Get devices assigned to the authenticated user
     * @endpoint  GET /api/my-device
     * @param    category Category to filter by (default "")
     * @param    status Status to filter by (default "")
     * @param    search Name or keyword to search (default "")
     * @param    page Page number (default 1)
     * @return   PageResponse<DeviceInfoView> containing the user's device information
     */
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

    /**
     * @description Create a new device
     * @endpoint  POST /api/admin/device
     * @param    request DeviceDto containing details of the device to create
     * @return   Boolean true if the device is created successfully
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/device")
    public SampleResponse<Boolean> createDevice(@RequestBody DeviceDto request) {
        try{
            deviceService.save(request);
            return new SampleResponse<>(true);
        } catch (Exception e){
            return new SampleResponse<>(false, e.getMessage());
        }
    }

    /**
     * @description Update an existing device
     * @endpoint  PUT /api/admin/device
     * @param    request UpdateDeviceRequest containing updated device information
     * @return   Boolean true if the device is updated successfully
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/admin/device")
    public SampleResponse<Boolean> updateDevice(@RequestBody UpdateDeviceRequest request) {
        return deviceService.updateDevice(request);
    }

    /**
     * @description Delete a device by its ID
     * @endpoint  DELETE /api/admin/device
     * @param    id ID of the device to delete
     * @return   Boolean true if the device is deleted successfully
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/admin/device")
    public SampleResponse<Boolean> deleteDevice(@RequestParam String id){
        return deviceService.deleteDeviceById(id);
    }

    /**
     * @description Request to return a device
     * @endpoint  POST /api/device-return
     * @param    request ActionByIdRequest containing the ID of the device to return
     * @return   Boolean true if the return request is created successfully
     */
    @PostMapping("/device-return")
    public SampleResponse<Boolean> returnDevice(@RequestBody ActionByIdRequest request){
        return deviceService.requestReturnDevice(request);
    }

    /**
     * @description Accept a device return request
     * @endpoint  POST /api/admin/accept-return
     * @param    request ActionByIdRequest containing the ID of the device to accept return
     * @return   Boolean true if the return request is accepted successfully
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/admin/accept-return")
    public SampleResponse<Boolean> acceptReturnDevice(@RequestBody ActionByIdRequest request){
        return deviceService.acceptReturnDevice(request);
    }
}
