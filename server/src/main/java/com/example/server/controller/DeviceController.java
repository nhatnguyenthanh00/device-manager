package com.example.server.controller;

import com.example.server.model.dto.DeviceDto;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.model.response.PageView;
import com.example.server.service.iservice.DeviceService;
import com.example.server.utils.CommonUtils;
import com.example.server.utils.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(Constants.ApiEndpoint.COMMON_BASE_PATH)
public class DeviceController {

    @Autowired
    DeviceService deviceService;

    @Autowired
    CommonUtils commonUtils;

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
    @GetMapping(Constants.ApiEndpoint.ADMIN_DEVICE_PATH)
    public ResponseEntity<PageView<DeviceInfoView>> getAllDevice(@RequestParam String category,
                                                                 @RequestParam(defaultValue = Constants.Common.EMPTY) Integer status,
                                                                 @RequestParam String search,
                                                                 @RequestParam(defaultValue = Constants.Common.NUMBER_1_STRING) Integer page){
        PageView<DeviceInfoView> data = deviceService.findAllDevice(search,category,status,page);
        return new ResponseEntity<>(data, HttpStatus.OK);
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
    @GetMapping(Constants.ApiEndpoint.MY_DEVICE_PATH)
    public ResponseEntity<PageView<DeviceInfoView>> getMyDevice(@RequestParam String category,
                                                                @RequestParam(defaultValue = Constants.Common.EMPTY) Integer status,
                                                                @RequestParam String search,
                                                                @RequestParam(defaultValue = Constants.Common.NUMBER_1_STRING) Integer page){
        String username = commonUtils.getCurrentUser().getUsername();
        PageView<DeviceInfoView> data = deviceService.findDeviceByUsername(username,search,category,status,page);
        return new ResponseEntity<>(data,HttpStatus.OK);
    }

    /**
     * @description Create or update a new device
     * @endpoint  POST /api/admin/device
     * @param    request DeviceDto containing details of the device to create
     * @return   Boolean true if the device is created successfully
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(Constants.ApiEndpoint.ADMIN_DEVICE_PATH)
    public ResponseEntity<?> createOrUpdateDevice(@RequestBody DeviceDto request) {
        return deviceService.save(request);
    }


    /**
     * @description Delete a device by its ID
     * @endpoint  DELETE /api/admin/device
     * @param    id ID of the device to delete
     * @return   Boolean true if the device is deleted successfully
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(Constants.ApiEndpoint.ADMIN_DEVICE_PATH)
    public ResponseEntity<?> deleteDevice(@RequestParam UUID id){
        return deviceService.deleteById(id);
    }

    /**
     * @description Request to return a device
     * @endpoint  POST /api/device-return
     * @param    id ID of the device to return
     * @return   Boolean true if the return request is created successfully
     */
    @PostMapping(Constants.ApiEndpoint.DEVICE_RETURN)
    public ResponseEntity<?> returnDevice(@RequestParam String id){
        return deviceService.requestReturnDevice(commonUtils.getCurrentUser().getUsername(),id);
    }

    /**
     * @description Accept a device return request
     * @endpoint  POST /api/admin/accept-return
     * @param    id of the device to accept return
     * @return   null if the return request is accepted successfully
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(Constants.ApiEndpoint.ADMIN_ACCEPT_RETURN)
    public ResponseEntity<?> acceptReturnDevice(@RequestParam String id){
        return deviceService.acceptReturnDevice(commonUtils.getCurrentUser().getUsername(),id);
    }

    /**
     * @description Refuse a device return request
     * @endpoint  POST /api/admin/accept-return
     * @param    id of the device to accept return
     * @return   null if the return request is refused successfully
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(Constants.ApiEndpoint.ADMIN_REFUSE_RETURN)
    public ResponseEntity<?> refuseReturnDevice(@RequestParam String id){
        return deviceService.refuseReturnDevice(commonUtils.getCurrentUser().getUsername(),id);
    }
}
