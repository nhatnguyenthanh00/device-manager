package com.example.server.service.iservice;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.entity.BkavUser;
import com.example.server.model.response.DetailUserResponse;
import com.example.server.model.response.PageResponse;
import com.example.server.model.response.SampleResponse;
import com.example.server.model.resquest.CreateNewUserRequest;
import com.example.server.model.resquest.ActionByIdRequest;
import com.example.server.model.resquest.DetailUserRequest;
import com.example.server.model.resquest.LoginRequest;
import com.example.server.service.EntityService;

import java.util.List;
import java.util.UUID;

public interface UserService extends EntityService<BkavUserDto> {

    PageResponse<BkavUserDto> getAllUser(String gender, String search, int page);

    BkavUserDto getById(UUID id);

    BkavUserDto getProfile(String userName);

//    BkavUser getByUsername(String username);

    Integer changePassWord(String username, String oldPassword, String newPassword);

    String verify(LoginRequest request);

    SampleResponse<Boolean> saveNewUser(CreateNewUserRequest request);
    SampleResponse<Boolean> updateUser(BkavUserDto userDto);

    SampleResponse<Boolean> deleteUserById(ActionByIdRequest request);

    SampleResponse<List<String>> getAllUsername();

    SampleResponse<DetailUserResponse> getDetailUser(DetailUserRequest request);
}
