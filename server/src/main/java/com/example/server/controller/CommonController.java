package com.example.server.controller;

import com.example.server.response.LoginResponse;
import com.example.server.resquest.LoginRequest;
import com.example.server.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommonController {

    @Autowired
    UserServiceImpl userServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        try {
            String token = userServiceImpl.verify(loginRequest);
            return ResponseEntity.ok(new LoginResponse(token));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(null));
        }
    }

    @GetMapping("/")
    public String test(HttpServletRequest request){
        return "WELCOME! " + request.getSession().getId();
    }


}
