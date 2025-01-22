package com.example.server.repository.dao.impl;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.response.PageResponse;
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
    public Optional<BkavUserDto> getById(UUID id) {
        BkavUser user = bkavUserRepository.findById(id).orElse(null);
        return Optional.of(mapperDto.toDto(user));
    }

    @Override
    public List<BkavUserDto> getAll() {
        List<BkavUser> users = bkavUserRepository.findAll();
        return users.
                stream().map(mapperDto::toDto)
                .collect(Collectors.toList());
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
    public PageResponse<BkavUserDto> getAllUserPaging(String gender, String name, int page){

        Pageable pageable = PageRequest.of(page - Constants.Common.NUMBER_1_INT,Constants.Common.NUMBER_5_INT);
        Page<BkavUser> pageUser = Page.empty(pageable);

        if(gender.isEmpty()){
            pageUser = bkavUserRepository.findBkavUserByNameContainsIgnoreCase(name, pageable);
        }
        if(gender.equals(Constants.Common.MALE)) pageUser = bkavUserRepository.findBkavUserByGenderAndNameContainingIgnoreCase(Gender.MALE,name, pageable);
        if(gender.equals(Constants.Common.FEMALE)) pageUser = bkavUserRepository.findBkavUserByGenderAndNameContainingIgnoreCase(Gender.FEMALE,name, pageable);

        if (pageUser.isEmpty()){
            return new PageResponse<>();
        }

        int totalItems = (int) pageUser.getTotalElements();
        int totalPages = pageUser.getTotalPages();
        List<BkavUserDto> pageUserDto = pageUser.getContent().stream().map(mapperDto::toDto).collect(Collectors.toList());
        return new PageResponse<>(pageUserDto, totalItems, totalPages);
    }

    @Override
    public BkavUserDto findByUserName(String username) {
        return mapperDto.toDto(bkavUserRepository.findByUsername(username));
    }

    @Override
    public BkavUserDto getProfileByUserName(String username) {
        BkavUser user = bkavUserRepository.findByUsername(username);
//        return new BkavUserDto(user);
        return mapperDto.toDto(user);
    }

    @Override
    public List<String> findAllUsername() {
        return bkavUserRepository.findAllUsernames();
    }

}
