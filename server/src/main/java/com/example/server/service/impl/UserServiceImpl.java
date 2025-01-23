package com.example.server.service.impl;

import com.example.server.config.security.UserPrincipal;
import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.model.response.DetailUserResponse;
import com.example.server.model.response.SampleResponse;
import com.example.server.model.resquest.*;
import com.example.server.repository.dao.idao.BkavUserDao;
import com.example.server.repository.dao.idao.DeviceDao;
import com.example.server.model.response.PageResponse;
import com.example.server.repository.view.DeviceInfoViewRepository;
import com.example.server.service.iservice.UserService;
import com.example.server.utils.MapperDto;
import com.example.server.utils.constants.Constants;
import com.example.server.utils.enums.Gender;
import com.example.server.utils.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    BkavUserDao bkavUserDao;

    @Autowired
    DeviceDao deviceDao;

    @Autowired
    DeviceInfoViewRepository deviceInfoViewRepository;

    @Autowired
    MapperDto mapperDto;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(Constants.Common.NUMBER_11_INT);

    @Override
    public PageResponse<BkavUserDto> getAllUser(String gender, String search, int page) {
        return bkavUserDao.getAllUserPaging(gender, search, page);
    }

    @Override
    public BkavUserDto getById(UUID id) {
        return bkavUserDao.getById(id).orElse(null);
    }

    @Override
    public BkavUserDto getProfile(String userName) {
        return bkavUserDao.getProfileByUserName(userName);
    }


    @Override
    public Integer changePassWord(String username, String oldPassword, String newPassword) {
        BkavUserDto user = bkavUserDao.findByUserName(username);
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
    public SampleResponse<Boolean> resetPassword(ResetPasswordRequest request) throws Exception {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserPrincipal userPrincipal = (UserPrincipal)principal;
        if(!encoder.matches(request.getAdminPassword(), userPrincipal.getPassword())) {
            throw new Exception("Admin password incorrect");
        }
        BkavUserDto userDto = new BkavUserDto();
        userDto.setUserId(request.getUserId());
        userDto.setPassword(request.getPassword());
        save(userDto);
        return new SampleResponse<>(true);
    }


    @Override
    public SampleResponse<Boolean> deleteUserById(String idRequest) {
        try{
            UUID id = UUID.fromString(idRequest);
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
    public SampleResponse<List<String>> getAllUsername() {
        return new SampleResponse<>(bkavUserDao.findAllUsername());
    }

    @Override
    public SampleResponse<DetailUserResponse> getDetailUser(DetailUserRequest request) {
        try{
            UUID id = UUID.fromString(request.getUserId());
            Integer page = request.getPage();
            BkavUserDto userDto = getById(id);
            if(userDto == null) return new SampleResponse<>(null,"Not found user");
            PageResponse<DeviceInfoView> pageResponse = deviceDao.getAllDeviceByUsername(userDto.getUsername(),page);
            return new SampleResponse<>(new DetailUserResponse(userDto,pageResponse));
        } catch (Exception e){
            return new SampleResponse<>(null,"Bad request");
        }
    }

    @Override
    public BkavUserDto save(BkavUserDto userDto) throws Exception {

        UUID userId = userDto.getUserId();
        // case create
        if(userId == null){
            //validate
            if(!validateInputCreateUser(userDto)) throw new Exception("Invalid input");
            //check username existed or not
            BkavUserDto findUser = bkavUserDao.findByUserName(userDto.getUsername());
            if(findUser != null) throw new Exception("Username already exists");
            BkavUserDto newUser = new BkavUserDto();
            newUser.setName(userDto.getName());
            newUser.setUsername(userDto.getUsername());
            newUser.setPassword(encoder.encode(userDto.getPassword()));
            newUser.setRole(Role.USER);
            newUser.setGender(userDto.getGender());
            return bkavUserDao.save(newUser);
        }
        // case update
        else{
            if(!validateInputUpdateUser(userDto)) throw new Exception("Invalid input");
            BkavUserDto findUser = bkavUserDao.getById(userId).orElse(null);
            if(findUser == null) throw new Exception("Not found user");
            String username = userDto.getUsername();;
            if(!isNullOrEmpty(username)){
                if(!username.equals(findUser.getUsername())){
                    BkavUserDto findExistUser = bkavUserDao.findByUserName(username);
                    if(findExistUser != null) throw new Exception("Username already exists");
                }
                findUser.setUsername(userDto.getUsername());
            }
            String name = userDto.getName();
            String password = userDto.getPassword();
            Role role = userDto.getRole();
            Gender gender = userDto.getGender();
            if(!isNullOrEmpty(name)) findUser.setName(name);
            if(!isNullOrEmpty(password)) findUser.setPassword(encoder.encode(password));
            if(role != null) findUser.setRole(role);
            if(gender != null) findUser.setGender(gender);

            return bkavUserDao.save(findUser);

        }
    }

    @Override
    public boolean deleteById(UUID id) {
        BkavUserDto findUser = bkavUserDao.getById(id).orElse(null);
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

    private static boolean isNullOrEmpty(String str){
        if(str == null) return true;
        if(str.trim().isEmpty()) return true;
        return false;
    }

    private static boolean validateInputCreateUser(BkavUserDto bkavUserDto){
        if(isNullOrEmpty(bkavUserDto.getName())
                || isNullOrEmpty(bkavUserDto.getUsername())) return false;
        String nameRegex = "^[\\p{L}\\s]+$";  // Matches letters (including Vietnamese) and spaces
        String usernameRegex = "^[a-zA-Z0-9]{6,}$";  // Alphanumeric, min 6 chars, no spaces
        if (!Pattern.matches(nameRegex, bkavUserDto.getName())) {
            return false;
        }
        if (!Pattern.matches(usernameRegex, bkavUserDto.getUsername())) {
            return false;
        }
        return true;
    }

    private static boolean validateInputUpdateUser(BkavUserDto bkavUserDto) {
        String nameRegex = "^[\\p{L}\\s]+$";  // Matches letters (including Vietnamese) and spaces
        String usernameRegex = "^[a-zA-Z0-9]{6,}$";  // Alphanumeric, min 6 chars, no spaces
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()])[A-Za-z\\d!@#$%^&*()]{8,}$";

        if (!isNullOrEmpty(bkavUserDto.getName())) {
            if (!Pattern.matches(nameRegex, bkavUserDto.getName())) {
                return false;
            }
        }

        if (!isNullOrEmpty(bkavUserDto.getUsername())) {
            if (!Pattern.matches(usernameRegex, bkavUserDto.getUsername())) {
                return false;
            }
        }

        if (!isNullOrEmpty(bkavUserDto.getPassword())) {
            if (!Pattern.matches(passwordRegex, bkavUserDto.getPassword())) {
                return false;
            }
        }

        return true;
    }

}
