package com.example.server.controller;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.entity.BkavUser;
import com.example.server.model.entity.Device;
import com.example.server.model.response.SampleResponse;
import com.example.server.model.resquest.CreateNewUserRequest;
import com.example.server.model.resquest.DeleteUserRequest;
import com.example.server.service.iservice.UserService;
import com.example.server.utils.constants.Constants;
import com.example.server.utils.enums.Gender;
import com.example.server.utils.enums.Role;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.model.response.PageResponse;
import com.example.server.model.resquest.UpdateDeviceRequest;
import com.example.server.service.impl.DeviceServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.example.server.utils.constants.Constants.ErrCode.ERROR_CODE_0;

@RestController
@RequestMapping(Constants.ApiEndpoint.ADMIN_BASE_PATH)
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminController {

    @Autowired
    UserService userService;

    @Autowired
    DeviceServiceImpl deviceService;

    @GetMapping(Constants.ApiEndpoint.USER_PATH)
    public SampleResponse<PageResponse<BkavUserDto>> getUserList(@RequestParam(required = false, defaultValue = "") String gender,
                                                                @RequestParam(required = false, defaultValue = "") String search,
                                                                @RequestParam(required = false, defaultValue = "1") String page){

        int pageNum = Constants.Common.NUMBER_1_INT ;
        try{
            pageNum = Integer.parseInt(page);
        } catch (Exception ignored){
        }
        PageResponse<BkavUserDto> data = userService.getAllUser(gender,search,pageNum);
        return new SampleResponse<>(ERROR_CODE_0,data);
    }

    @PostMapping(Constants.ApiEndpoint.USER_PATH)
    public SampleResponse<Boolean> createUser(@RequestBody CreateNewUserRequest request) {

        if(!Constants.Common.ALL_GENDER.contains(request.getGender())){
            return new SampleResponse<>(400,false,"Invalid gender");
        }

        BkavUser newUser = new BkavUser();
        newUser.setUsername(request.getUsername());
        newUser.setName(request.getName());
        newUser.setPassword(request.getPassword());
        newUser.setRole(Role.USER);
        newUser.setGender(request.getGender().equals(Constants.Common.MALE) ? Gender.MALE : Gender.FEMALE);

        BkavUser saveUser = userService.save(newUser);
        if(saveUser == null){
            return new SampleResponse<>(400,false,"Username already exists");
        }
        return new SampleResponse<>(0,true);
    }

    @DeleteMapping(Constants.ApiEndpoint.USER_PATH)
    public SampleResponse<Boolean> deleteUser(@RequestBody DeleteUserRequest request){
        try{
            UUID id = UUID.fromString(request.getUserId());
            boolean check = userService.deleteById(id);
            if (check){
                return new SampleResponse<>(0, true);
            }
            return new SampleResponse<>(400,false,"Delete user fail");
        } catch (Exception e){
            return new SampleResponse<>(400,false,"Delete user fail");
        }
    }



    @GetMapping(Constants.ApiEndpoint.DEVICE_PATH)
    public ResponseEntity<List<DeviceInfoView>> getAllDevice(){
        return ResponseEntity.ok(deviceService.getAllDevice());
    }

    @PostMapping(Constants.ApiEndpoint.DEVICE_PATH)
    public ResponseEntity<Device> createDevice(@RequestBody Device device) {
        return ResponseEntity.ok(deviceService.save(device));
    }

    @PutMapping(Constants.ApiEndpoint.DEVICE_PATH)
    public ResponseEntity<String> updateDevice(@RequestBody UpdateDeviceRequest request) {
        boolean check = deviceService.updateDevice(request);
        if(check) return  ResponseEntity.ok("Success");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Fail");
    }

}
