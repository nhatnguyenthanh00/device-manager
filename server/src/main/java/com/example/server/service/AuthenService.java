package com.example.server.service;

import com.example.server.config.security.JwtService;
import com.example.server.config.security.UserPrincipal;
import com.example.server.model.response.LoginResponse;
import com.example.server.model.response.SampleResponse;
import com.example.server.model.resquest.LoginRequest;
import com.example.server.utils.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
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

    public SampleResponse<LoginResponse> verify(LoginRequest request) {

        try {
            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            if (authentication.isAuthenticated()) {
                UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
                String token = jwtService.generateToken(userPrincipal);
                return new SampleResponse<>(new LoginResponse(token));
            }
            return new SampleResponse<>(null, Constants.ErrorMessage.WRONG_USERNAME_OR_PASSWORD);
        } catch (Exception e) {
            return new SampleResponse<>(null, e.getMessage());
        }
    }
}
