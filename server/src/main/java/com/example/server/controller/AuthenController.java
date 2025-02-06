package com.example.server.controller;

import com.example.server.config.security.UserPrincipal;
import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.response.LoginResponse;
import com.example.server.model.resquest.ChangePasswordRequest;
import com.example.server.model.resquest.LoginRequest;
import com.example.server.service.AuthenService;
import com.example.server.service.iservice.UserService;
import com.example.server.utils.CommonUtils;
import com.example.server.utils.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.ApiEndpoint.COMMON_BASE_PATH)
public class AuthenController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenService authenService;

    @Autowired
    CommonUtils commonUtils;

    /**
     * @description Authenticate user and generate a JWT token
     * @endpoint  POST /api/login
     * @param    request LoginRequest containing username and password
     * @return   LoginResponse with JWT token if authentication is successful
     */
    @PostMapping(Constants.ApiEndpoint.LOGIN_PATH)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        return authenService.verify(request);
    }

    /**
     * @description Retrieve the profile of the authenticated user
     * @endpoint  GET /api/profile
     * @return   BkavUserDto containing the user's profile information
     */
    @GetMapping(Constants.ApiEndpoint.PROFILE_PATH)
    public ResponseEntity<BkavUserDto> findMyProfile(){
        UserPrincipal userPrincipal = commonUtils.getCurrentUser();
        BkavUserDto userDto = userService.findProfile(userPrincipal.getUsername());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    /**
     * @description Change the password for the authenticated user
     * @endpoint  POST /api/changePassword
     * @param    request ChangePasswordRequest containing old and new passwords
     * @return   Boolean true if the password is changed successfully
     */
    @PostMapping(Constants.ApiEndpoint.CHANGE_PASSWORD_PATH)
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest request){
        String userName = commonUtils.getCurrentUser().getUsername();
        return userService.changePassWord(userName,request.getOldPassword(),request.getNewPassword());
    }

}
