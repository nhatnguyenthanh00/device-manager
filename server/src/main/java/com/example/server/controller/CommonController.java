package com.example.server.controller;

import com.example.server.config.security.JwtService;
import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.entity.BkavUser;
import com.example.server.model.response.LoginResponse;
import com.example.server.model.response.SampleResponse;
import com.example.server.model.resquest.ChangePasswordRequest;
import com.example.server.model.resquest.LoginRequest;
import com.example.server.service.iservice.UserService;
import com.example.server.utils.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.token.TokenService;
import org.springframework.web.bind.annotation.*;


import java.net.http.HttpRequest;

import static com.example.server.utils.constants.Constants.ErrCode.*;

@RestController
@RequestMapping(Constants.ApiEndpoint.COMMON_BASE_PATH)
public class CommonController {

    @Autowired
    UserService userService;

    @Autowired
    JwtService jwtService;

    @PostMapping(Constants.ApiEndpoint.LOGIN_PATH)
    public SampleResponse<LoginResponse> login(@RequestBody LoginRequest request){
        try {
            String token = userService.verify(request);
            LoginResponse data = new LoginResponse(token);
            return new SampleResponse<>(ERROR_CODE_0,data);
        } catch (Exception e) {
            return new SampleResponse<>(ERROR_CODE_401,null,"Wrong username or password");
        }
    }

    @GetMapping("/profile")
    public SampleResponse<BkavUserDto> getProfile( @RequestHeader("Authorization") String authorizationHeader){

        String token = authorizationHeader.substring(7);
        String userName = jwtService.extractUserName(token);
        BkavUserDto user = userService.getProfile(userName);
        return new SampleResponse<>(0,user);
    }

    @PostMapping("/changePassword")
    public SampleResponse<Boolean> changePassword(@RequestHeader("Authorization") String authorizationHeader,@RequestBody ChangePasswordRequest request){
        String token = authorizationHeader.substring(7);
        String userName = jwtService.extractUserName(token);
        Integer check = userService.changePassWord(userName, request.getOldPassword(), request.getNewPassword());
        if(check == 1) return new SampleResponse<>(0, true);
        if(check == -1) return new SampleResponse<>(400,false, "Password incorrect");
        return new SampleResponse<>(500,false, "Internal Server Error");
    }

    @GetMapping(Constants.ApiEndpoint.EMPTY_PATH)
    public String test(){
        return "WELCOME TO LUCIFER";
    }


}
