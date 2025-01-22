package com.example.server.service.iservice;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.entity.BkavUser;
import com.example.server.model.response.DetailUserResponse;
import com.example.server.model.response.PageResponse;
import com.example.server.model.response.SampleResponse;
import com.example.server.model.resquest.*;
import com.example.server.service.EntityService;

import java.util.List;
import java.util.UUID;

public interface UserService extends EntityService<BkavUserDto> {

    PageResponse<BkavUserDto> getAllUser(String gender, String search, int page);

    BkavUserDto getById(UUID id);

    BkavUserDto getProfile(String userName);

//    BkavUser getByUsername(String username);

    Integer changePassWord(String username, String oldPassword, String newPassword);

    SampleResponse<Boolean> resetPassword(ResetPasswordRequest request) throws Exception;
    SampleResponse<Boolean> deleteUserById(ActionByIdRequest request);

    SampleResponse<List<String>> getAllUsername();

    SampleResponse<DetailUserResponse> getDetailUser(DetailUserRequest request);
}
