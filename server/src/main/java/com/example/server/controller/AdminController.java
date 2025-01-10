package com.example.server.controller;

import com.example.server.model.BkavUser;
import com.example.server.model.Device;
import com.example.server.model.enums.Role;
import com.example.server.model.view.DeviceInfoView;
import com.example.server.resquest.GetUserListRequest;
import com.example.server.resquest.UpdateDeviceRequest;
import com.example.server.service.impl.UserServiceImpl;
import com.example.server.service.impl.DeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    UserServiceImpl userServiceImpl;

    @Autowired
    DeviceServiceImpl deviceService;

//    @GetMapping("/user-list")
    @RequestMapping(value = "/user-list", method = RequestMethod.GET)
    public ResponseEntity<List<?>> getUserList(@RequestParam(required = false, defaultValue = "ALL") String gender, @RequestParam(required = false, defaultValue = "") String search){
        System.out.println("NTN");
        if(gender!=null) System.out.println(gender);
        if(search!=null) System.out.println(search);
        return ResponseEntity.ok(userServiceImpl.getAllUser(gender,search));
    }

    @PostMapping("/user")
    public ResponseEntity<BkavUser> createUser(@RequestBody BkavUser bkavUser) {
        bkavUser.setRole(Role.USER);
        return ResponseEntity.ok(userServiceImpl.save(bkavUser));
    }

    @GetMapping("/device")
    public ResponseEntity<List<DeviceInfoView>> getAllDevice(){
        return ResponseEntity.ok(deviceService.getAllDevice());
    }

    @PostMapping("/device")
    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
        return ResponseEntity.ok(deviceService.save(device));
    }

    @PutMapping("/device")
    public ResponseEntity<Device> updateDevice(@RequestBody UpdateDeviceRequest request) {
        boolean check = deviceService.updateDevice(request);
        if(check) return  ResponseEntity.ok(null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
    }
}
