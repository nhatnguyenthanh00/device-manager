package com.example.server.repository.dao.idao;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.response.PageResponse;
import com.example.server.repository.dao.EntityDao;
import com.example.server.model.entity.BkavUser;

import java.util.List;

public interface BkavUserDao extends EntityDao<BkavUserDto> {
    PageResponse<BkavUserDto> getAllUserPaging(String gender, String name, int page);

    BkavUserDto findByUserName(String username);

    BkavUserDto getProfileByUserName(String username);

    List<String> findAllUsername();
}
