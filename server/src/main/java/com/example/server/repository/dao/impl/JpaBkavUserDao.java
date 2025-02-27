package com.example.server.repository.dao.impl;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.dto.SelectUser;
import com.example.server.model.response.PageView;
import com.example.server.repository.dao.idao.BkavUserDao;
import com.example.server.model.entity.BkavUser;
import com.example.server.repository.BkavUserRepository;
import com.example.server.utils.MapperDto;
import com.example.server.utils.constants.Constants;
import com.example.server.utils.enums.Gender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class JpaBkavUserDao implements BkavUserDao {


    @Autowired
    BkavUserRepository bkavUserRepository;

    @Autowired
    MapperDto mapperDto;


    @Override
    public Optional<BkavUserDto> findById(UUID id) {
        BkavUser user = bkavUserRepository.findById(id).orElse(null);
        return Optional.ofNullable(mapperDto.toDto(user));
    }

    @Override
    public BkavUserDto save(BkavUserDto bkavUserDto) {
        BkavUser saveUser = mapperDto.toBkavUser(bkavUserDto);
        BkavUser user = bkavUserRepository.save(saveUser);
        return mapperDto.toDto(user);
    }

    @Override
    public void deleteById(UUID id) {
        bkavUserRepository.deleteById(id);
    }


    @Override
    public PageView<BkavUserDto> findAllUserPaging(String gender, String name, int page){

        Pageable pageable = PageRequest.of(page - Constants.Common.NUMBER_1_INT,Constants.Common.NUMBER_5_INT);
        Page<BkavUser> pageUser = Page.empty(pageable);

        if(gender.isEmpty()){
            pageUser = bkavUserRepository.findBkavUserByNameContainsIgnoreCase(name, pageable);
        }
        if(gender.equals(Constants.Common.MALE)) pageUser = bkavUserRepository.findBkavUserByGenderAndNameContainingIgnoreCase(Gender.MALE,name, pageable);
        if(gender.equals(Constants.Common.FEMALE)) pageUser = bkavUserRepository.findBkavUserByGenderAndNameContainingIgnoreCase(Gender.FEMALE,name, pageable);

        if (pageUser.isEmpty()){
            return new PageView<>();
        }

        int totalItems = (int) pageUser.getTotalElements();
        int totalPages = pageUser.getTotalPages();
        List<BkavUserDto> pageUserDto = pageUser.getContent().stream().map(mapperDto::toDto).collect(Collectors.toList());
        pageUserDto.forEach(user -> user.setPassword(null));
        return new PageView<>(pageUserDto, totalItems, totalPages);
    }

    @Override
    public BkavUserDto findByUserName(String username) {
        return mapperDto.toDto(bkavUserRepository.findByUsername(username));
    }

    @Override
    public BkavUserDto findProfileByUserName(String username) {
        BkavUser user = bkavUserRepository.findByUsername(username);
        return mapperDto.toDto(user);
    }


    @Override
    public List<SelectUser> findAllSelectUser() {
        return bkavUserRepository.findAllSelectUser();
    }

}
