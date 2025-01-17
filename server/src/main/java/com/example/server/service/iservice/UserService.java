package com.example.server.service.iservice;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.entity.BkavUser;
import com.example.server.model.response.PageResponse;
import com.example.server.model.resquest.LoginRequest;
import com.example.server.service.EntityService;

import java.util.UUID;

public interface UserService extends EntityService<BkavUser> {

    PageResponse<BkavUserDto> getAllUser(String gender, String search, int page);

    BkavUserDto getProfile(String userName);

    BkavUser getByUsername(String username);

    Integer changePassWord(String username, String oldPassword, String newPassword);

    String verify(LoginRequest request);
}
