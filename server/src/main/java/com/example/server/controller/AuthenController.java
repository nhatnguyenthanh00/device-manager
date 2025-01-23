package com.example.server.controller;

import com.example.server.config.security.JwtService;
import com.example.server.config.security.UserPrincipal;
import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.response.LoginResponse;
import com.example.server.model.response.SampleResponse;
import com.example.server.model.resquest.ChangePasswordRequest;
import com.example.server.model.resquest.LoginRequest;
import com.example.server.service.AuthenService;
import com.example.server.service.iservice.UserService;
import com.example.server.utils.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Constants.ApiEndpoint.COMMON_BASE_PATH)
public class AuthenController {

    @Autowired
    UserService userService;

    @Autowired
    AuthenService authenService;

    /**
     * @description Authenticate user and generate a JWT token
     * @endpoint  POST /api/login
     * @param    request LoginRequest containing username and password
     * @return   LoginResponse with JWT token if authentication is successful
     */
    @PostMapping(Constants.ApiEndpoint.LOGIN_PATH)
    public SampleResponse<LoginResponse> login(@RequestBody LoginRequest request){
        try {
            String token = authenService.verify(request);
            LoginResponse data = new LoginResponse(token);
            return new SampleResponse<>(data);
        } catch (Exception e) {
            return new SampleResponse<>(null,"Wrong username or password");
        }
    }

    /**
     * @description Retrieve the profile of the authenticated user
     * @endpoint  GET /api/profile
     * @return   BkavUserDto containing the user's profile information
     */
    @GetMapping("/profile")
    public SampleResponse<BkavUserDto> getMyProfile(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserPrincipal userPrincipal = (UserPrincipal)principal;
        BkavUserDto userDto = userService.getProfile(userPrincipal.getUsername());
        return new SampleResponse<>(userDto);
    }

    /**
     * @description Change the password for the authenticated user
     * @endpoint  POST /api/changePassword
     * @param    request ChangePasswordRequest containing old and new passwords
     * @return   Boolean true if the password is changed successfully
     */
    @PostMapping("/changePassword")
    public SampleResponse<Boolean> changePassword(@RequestBody ChangePasswordRequest request){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserPrincipal userPrincipal = (UserPrincipal)principal;
        String userName = userPrincipal.getUsername();
        Integer check = userService.changePassWord(userName, request.getOldPassword(), request.getNewPassword());
        if(check == 1) return new SampleResponse<>(true);
        if(check == -1) return new SampleResponse<>(false, "Password incorrect");
        return new SampleResponse<>(false, "Internal Server Error");
    }
}
