package com.example.server.service;

import com.example.server.config.security.JwtService;
import com.example.server.config.security.UserPrincipal;
import com.example.server.model.response.LoginResponse;
import com.example.server.model.resquest.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    public ResponseEntity<LoginResponse> verify(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String token = jwtService.generateToken(userPrincipal);
        return new ResponseEntity<>(new LoginResponse(token), HttpStatus.OK);

    }
}
