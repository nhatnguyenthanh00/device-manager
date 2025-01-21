package com.example.server.service.impl;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.response.SampleResponse;
import com.example.server.model.resquest.CreateNewUserRequest;
import com.example.server.model.resquest.DeleteByIdRequest;
import com.example.server.repository.dao.idao.BkavUserDao;
import com.example.server.repository.dao.impl.JpaBkavUserDao;
import com.example.server.model.entity.BkavUser;
import com.example.server.config.security.UserPrincipal;
import com.example.server.repository.BkavUserRepository;
import com.example.server.model.response.PageResponse;
import com.example.server.model.resquest.LoginRequest;
import com.example.server.config.security.JwtService;
import com.example.server.repository.view.DeviceInfoViewRepository;
import com.example.server.service.iservice.UserService;
import com.example.server.utils.MapperDto;
import com.example.server.utils.constants.Constants;
import com.example.server.utils.enums.Gender;
import com.example.server.utils.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    BkavUserDao bkavUserDao;

    @Autowired
    JpaBkavUserDao jpaBkavUserDao;

    @Autowired
    BkavUserRepository bkavUserRepository;

    @Autowired
    DeviceInfoViewRepository deviceInfoViewRepository;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    MapperDto mapperDto;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(Constants.Common.NUMBER_11_INT);

    @Override
    public PageResponse<BkavUserDto> getAllUser(String gender, String search, int page) {
        return bkavUserDao.getAllUserPaging(gender, search, page);
    }

    @Override
    public BkavUserDto getById(UUID id) {
        BkavUser user = bkavUserDao.getById(id).orElse(null);
        if (user == null) return null;
//        return new BkavUserDto(user);
        return mapperDto.toDto(user);
    }

    @Override
    public BkavUserDto getProfile(String userName) {
        return bkavUserDao.getProfileByUserName(userName);
    }

    @Override
    public BkavUser getByUsername(String username) {
        return bkavUserDao.findByUserName(username);
    }

    @Override
    public Integer changePassWord(String username, String oldPassword, String newPassword) {
        BkavUser user = bkavUserDao.findByUserName(username);
        if (!encoder.matches(oldPassword, user.getPassword())) {
            return -1;
        }
        String encodeNewPassword = encoder.encode(newPassword);
        user.setPassword(encodeNewPassword);
        try {
            bkavUserDao.save(user);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public String verify(LoginRequest request) {
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if (authentication.isAuthenticated()) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return jwtService.generateToken(userPrincipal);
        }
        return null;
    }

    @Override
    public SampleResponse<Boolean> saveNewUser(CreateNewUserRequest request) {

        if(!Constants.Common.ALL_GENDER.contains(request.getGender())){
            return new SampleResponse<>(false,"Invalid gender");
        }

        BkavUser newUser = new BkavUser();
        newUser.setUsername(request.getUsername());
        newUser.setName(request.getName());
        newUser.setPassword(request.getPassword());
        newUser.setRole(Role.USER);
        newUser.setGender(request.getGender().equals(Constants.Common.MALE) ? Gender.MALE : Gender.FEMALE);
        BkavUser user = save(newUser);
        if (user == null) return new SampleResponse<>(false, "Username already exists");
        return new SampleResponse<>(true);
    }

    @Override
    public SampleResponse<Boolean> deleteUserById(DeleteByIdRequest request) {
        try{
            UUID id = UUID.fromString(request.getId());
            boolean check = deleteById(id);
            if (check){
                return new SampleResponse<>(true);
            }
            return new SampleResponse<>(false,"Delete user fail");
        } catch (Exception e){
            return new SampleResponse<>(false,"Delete user fail");
        }
    }

    @Override
    public BkavUser save(BkavUser user) {

        BkavUser findUser = bkavUserDao.findByUserName(user.getUsername());
        if (findUser != null) {
            return null;
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return bkavUserDao.save(user);
    }

    @Override
    public boolean deleteById(UUID id) {
        BkavUser findUser = bkavUserDao.getById(id).orElse(null);
        if (findUser == null) return false;
        if (findUser.getRole().name().equals("USER")) {
            if(deviceInfoViewRepository.countDeviceInfoViewByUsername(findUser.getUsername()) > 0){
                return false;
            }
            bkavUserDao.deleteById(id);
            return true;
        }
        return false;
    }
}
