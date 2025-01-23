package com.example.server.repository.dao.idao;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.dto.SelectUser;
import com.example.server.model.response.PageView;
import com.example.server.repository.dao.EntityDao;

import java.util.List;

public interface BkavUserDao extends EntityDao<BkavUserDto> {
    PageView<BkavUserDto> findAllUserPaging(String gender, String name, int page);

    BkavUserDto findByUserName(String username);

    BkavUserDto findProfileByUserName(String username);

    List<SelectUser> findAllSelectUser();
}
