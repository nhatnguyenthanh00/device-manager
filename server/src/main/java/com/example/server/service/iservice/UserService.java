package com.example.server.service.iservice;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.dto.SelectUser;
import com.example.server.model.response.PageView;
import com.example.server.model.resquest.*;
import com.example.server.service.EntityService;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface UserService extends EntityService<BkavUserDto> {

    PageView<BkavUserDto> findAllUser(String gender, String search, int page);

    BkavUserDto findById(UUID id);

    BkavUserDto findProfile(String userName);

//    BkavUser getByUsername(String username);

    ResponseEntity<?> changePassWord(String username, String oldPassword, String newPassword);

    ResponseEntity<?> resetPassword(ResetPasswordRequest request);

    ResponseEntity<?> findDetailUser(String userId, int page);

    List<SelectUser> findAllSelectUser();
}
