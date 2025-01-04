package com.example.server.controller;

import com.example.server.model.Account;
import com.example.server.model.Device;
import com.example.server.model.enums.Role;
import com.example.server.resquest.UpdateDeviceRequest;
import com.example.server.service.AccountService;
import com.example.server.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    AccountService accountService;

    @Autowired
    DeviceService deviceService;

    @GetMapping("/user")
    public ResponseEntity<List<Account>> getAllUsers(){
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @PostMapping("/user")
    public ResponseEntity<Account> createUser(@RequestBody Account account) {
        account.setRole(Role.USER);
        return ResponseEntity.ok(accountService.saveAccount(account));
    }

    @GetMapping("/device")
    public ResponseEntity<List<Device>> getAllDevices(){
        return ResponseEntity.ok(deviceService.getAllDevice());
    }

    @PostMapping("/device")
    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
        return ResponseEntity.ok(deviceService.saveDevice(device));
    }

    @PutMapping("/device")
    public ResponseEntity<Device> updateDevice(@RequestBody UpdateDeviceRequest request) {
        boolean check = deviceService.updateDevice(request);
        if(check) return  ResponseEntity.ok(null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(null);
    }
}
