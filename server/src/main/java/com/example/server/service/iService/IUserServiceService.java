package com.example.server.service.iService;

import com.example.server.model.BkavUser;
import com.example.server.resquest.LoginRequest;
import com.example.server.service.EntityService;

import java.util.List;

public interface IUserServiceService extends EntityService<BkavUser> {

    public List<?> getAllUser(String gender, String search);

    String verify(LoginRequest request);
}
