package com.example.server.repository.dao.idao;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.response.PageResponse;
import com.example.server.repository.dao.EntityDao;
import com.example.server.model.entity.BkavUser;

public interface BkavUserDao extends EntityDao<BkavUser> {
    PageResponse<BkavUserDto> getAllUserPaging(String gender, String name, int page);

    BkavUser findByUserName(String username);

    BkavUserDto getProfileByUserName(String username);
}
