package com.example.server.service.impl;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.repository.dao.idao.BkavUserDao;
import com.example.server.repository.dao.impl.JpaBkavUserDao;
import com.example.server.model.entity.BkavUser;
import com.example.server.config.security.UserPrincipal;
import com.example.server.repository.BkavUserRepository;
import com.example.server.model.response.PageResponse;
import com.example.server.model.resquest.LoginRequest;
import com.example.server.config.security.JwtService;
import com.example.server.service.iservice.UserService;
import com.example.server.utils.constants.Constants;
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
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(Constants.Common.NUMBER_11_INT);

    @Override
    public PageResponse<BkavUserDto> getAllUser(String gender, String search, int page) {
        return bkavUserDao.getAllUserPaging(gender,search,page);
    }

    @Override
    public BkavUserDto getProfile(String userName){
        return bkavUserDao.getProfileByUserName(userName);
    }

    @Override
    public BkavUser getByUsername(String username) {
        return bkavUserDao.findByUserName(username);
    }

    @Override
    public Integer changePassWord(String username, String oldPassword, String newPassword) {
        BkavUser user = bkavUserDao.findByUserName(username);
        if(!encoder.matches(oldPassword,user.getPassword())){
            return -1;
        }
        String encodeNewPassword = encoder.encode(newPassword);
        user.setPassword(encodeNewPassword);
        try{
            BkavUser saveUser = bkavUserDao.save(user);
            return 1;
        } catch (Exception e){
            return 0;
        }
    }

    @Override
    public String verify(LoginRequest request){
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        if (authentication.isAuthenticated()){
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return jwtService.generateToken(userPrincipal);
        }
        return null;
    }

    @Override
    public BkavUser save(BkavUser user) {

        BkavUser findUser = bkavUserDao.findByUserName(user.getUsername());
        if(findUser != null){
            return null;
        }
        user.setPassword(encoder.encode(user.getPassword()));
        return bkavUserDao.save(user);
    }

    @Override
    public boolean deleteById(UUID id) {
        BkavUser findUser = bkavUserDao.getById(id).orElse(null);
        if(findUser == null) return false;
        if(findUser.getRole().name().equals("USER")){
            if(!findUser.getDevices().isEmpty()){
                return false;
            }
            bkavUserDao.deleteById(id);
            return true;
        }
        return false;
    }
}
