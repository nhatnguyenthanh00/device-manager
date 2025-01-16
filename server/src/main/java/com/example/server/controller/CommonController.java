package com.example.server.controller;

import com.example.server.model.response.LoginResponse;
import com.example.server.model.response.SampleResponse;
import com.example.server.model.resquest.LoginRequest;
import com.example.server.service.iservice.UserService;
import com.example.server.utils.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import static com.example.server.utils.constants.Constants.ErrCode.*;

@RestController
@RequestMapping(Constants.ApiEndpoint.COMMON_BASE_PATH)
public class CommonController {

    @Autowired
    UserService userService;

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

    @GetMapping(Constants.ApiEndpoint.EMPTY_PATH)
    public String test(){
        return "WELCOME TO LUCIFER";
    }


}
