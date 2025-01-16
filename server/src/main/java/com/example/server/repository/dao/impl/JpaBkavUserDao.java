package com.example.server.repository.dao.impl;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.response.PageResponse;
import com.example.server.repository.dao.idao.BkavUserDao;
import com.example.server.model.entity.BkavUser;
import com.example.server.repository.BkavUserRepository;
import com.example.server.utils.constants.Constants;
import com.example.server.utils.enums.Gender;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;


@Component
public class JpaBkavUserDao implements BkavUserDao {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    BkavUserRepository bkavUserRepository;

//    @Autowired
//    BkavUserDeviceViewRepository bkavUserDeviceViewRepository;

    @Override
    public Optional<BkavUser> getById(UUID id) {
        return bkavUserRepository.findById(id);
    }

    @Override
    public List<BkavUser> getAll() {
        return bkavUserRepository.findAll();
    }

    @Override
    public BkavUser save(BkavUser bkavUser) {
        return bkavUserRepository.save(bkavUser);
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
        List<BkavUserDto> pageUserDto = pageUser.getContent().stream().map(BkavUserDto::new).collect(Collectors.toList());
        return new PageResponse<>(pageUserDto, totalItems, totalPages);
    }

    @Override
    public BkavUser findByUserName(String username) {
        return bkavUserRepository.findByUsername(username);
    }

}
