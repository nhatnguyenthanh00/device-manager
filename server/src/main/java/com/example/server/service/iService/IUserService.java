package com.example.server.service.iService;

import com.example.server.model.BkavUser;
import com.example.server.resquest.LoginRequest;

import java.util.List;

public interface IUserService {

    public List<?> getAllUser();

    String verify(LoginRequest request);
}
