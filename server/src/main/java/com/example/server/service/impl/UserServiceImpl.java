package com.example.server.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.example.server.config.security.UserPrincipal;
import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.dto.SelectUser;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.model.response.DetailUserResponse;
import com.example.server.model.response.SampleResponse;
import com.example.server.model.resquest.*;
import com.example.server.repository.dao.idao.BkavUserDao;
import com.example.server.repository.dao.idao.DeviceDao;
import com.example.server.model.response.PageView;
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
    public PageView<BkavUserDto> findAllUser(String gender, String search, int page) {
        return bkavUserDao.findAllUserPaging(gender, search, page);
    }

    @Override
    public BkavUserDto findById(UUID id) {
        return bkavUserDao.findById(id).orElse(null);
    }

    @Override
    public BkavUserDto findProfile(String userName) {
        return bkavUserDao.findProfileByUserName(userName);
    }


    @Override
    public SampleResponse<Boolean> changePassWord(String username, String oldPassword, String newPassword) {

        try {
            BkavUserDto user = bkavUserDao.findByUserName(username);
            if(newPassword.equals(oldPassword))
                return new SampleResponse<>(false,Constants.ErrorMessage.NEW_PASSWORD_SAME_OLD_PASSWORD);
            if (!encoder.matches(oldPassword, user.getPassword())) {
                return new SampleResponse<>(false, Constants.ErrorMessage.INCORRECT_PASSWORD);
            }
            String encodeNewPassword = encoder.encode(newPassword);
            user.setPassword(encodeNewPassword);
            bkavUserDao.save(user);
            return new SampleResponse<>(true);
        } catch (Exception e) {
            return new SampleResponse<>(false, e.getMessage());
        }
    }

    @Override
    public SampleResponse<Boolean> resetPassword(ResetPasswordRequest request) {

        try {
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UserPrincipal userPrincipal = (UserPrincipal) principal;
            if (!encoder.matches(request.getAdminPassword(), userPrincipal.getPassword())) {
                throw new Exception(Constants.ErrorMessage.ADMIN_PASSWORD_INCORRECT);
            }
            BkavUserDto userDto = new BkavUserDto();
            userDto.setUserId(request.getUserId());
            userDto.setPassword(request.getPassword());
            save(userDto);
            return new SampleResponse<>(true);
        } catch (Exception e) {
            return new SampleResponse<>(false, e.getMessage());
        }
    }


    @Override
    public SampleResponse<DetailUserResponse> findDetailUser(String userId, int page) {
        try {
            UUID id = UUID.fromString(userId);
            BkavUserDto userDto = findById(id);
            if (userDto == null) return new SampleResponse<>(null, Constants.ErrorMessage.NOT_FOUND_USER);
            PageView<DeviceInfoView> pageView = deviceDao.findAllDeviceByUsername(userDto.getUsername(), page);
            return new SampleResponse<>(new DetailUserResponse(userDto, pageView));
        } catch (Exception e) {
            return new SampleResponse<>(null, e.getMessage());
        }
    }

    @Override
    public SampleResponse<List<SelectUser>> findAllSelectUser() {
        try{
            return new SampleResponse<>(bkavUserDao.findAllSelectUser());
        } catch (Exception e){
            return new SampleResponse<>(null,e.getMessage());
        }
    }

    @Override
    public SampleResponse<BkavUserDto> save(BkavUserDto userDto) {

        UUID userId = userDto.getUserId();
        // case create
        if (userId == null) {
            if (!validateInputCreateUser(userDto)) {
                return new SampleResponse<>(null, Constants.ErrorMessage.INVALID_INPUT);
            }
            BkavUserDto findUser = bkavUserDao.findByUserName(userDto.getUsername());
            if (findUser != null) {
                return new SampleResponse<>(null, Constants.ErrorMessage.USERNAME_EXISTED);
            }
            String password = userDto.getPassword();
            userDto.setPassword(encoder.encode(password));
            userDto.setRole(Role.USER);
            return new SampleResponse<>(bkavUserDao.save(userDto));
        }
        // case update
        else {
            if (!validateInputUpdateUser(userDto)) {
                return new SampleResponse<>(null, Constants.ErrorMessage.INVALID_INPUT);

            }
            BkavUserDto findUser = bkavUserDao.findById(userId).orElse(null);
            if (findUser == null) {
                return new SampleResponse<>(null, Constants.ErrorMessage.NOT_FOUND_USER);
            }
            String username = userDto.getUsername();

            if (!username.equals(findUser.getUsername())) {
                BkavUserDto findExistUser = bkavUserDao.findByUserName(username);
                if (findExistUser != null) {
                    return new SampleResponse<>(null, Constants.ErrorMessage.USERNAME_EXISTED);
                }
            }
            findUser.setUsername(userDto.getUsername());
            String name = userDto.getName();
            String password = userDto.getPassword();
            Role role = userDto.getRole();
            Gender gender = userDto.getGender();
            if (!StringUtil.isNullOrEmpty(name)) findUser.setName(name);
            if (!StringUtil.isNullOrEmpty(password)) findUser.setPassword(encoder.encode(password));
            if (role != null) findUser.setRole(role);
            if (gender != null) findUser.setGender(gender);

            return new SampleResponse<>(bkavUserDao.save(findUser));

        }
    }

    @Override
    public SampleResponse<Boolean> deleteById(UUID id) {
        try {
            BkavUserDto findUser = bkavUserDao.findById(id).orElse(null);
            if (findUser == null) return new SampleResponse<>(false,Constants.ErrorMessage.NOT_FOUND_USER);
            if (findUser.getRole().name().equals("USER")) {
                if (deviceInfoViewRepository.countDeviceInfoViewByUsername(findUser.getUsername()) > 0) {
                    return new SampleResponse<>(false,Constants.ErrorMessage.USER_NOW_CONTROL_DEVICES);
                }
                bkavUserDao.deleteById(id);
                return new SampleResponse<>(true);
            }
            return new SampleResponse<>(true,"Can't delete admin");
        } catch (Exception e){
            return new SampleResponse<>(false,e.getMessage());
        }
    }

    private static boolean validateInputCreateUser(BkavUserDto bkavUserDto) {
        if (StringUtil.isNullOrEmpty(bkavUserDto.getName())
                || StringUtil.isNullOrEmpty(bkavUserDto.getUsername())) return false;
        if (!Pattern.matches(Constants.RegexString.NAME_REGEX, bkavUserDto.getName())) {
            return false;
        }
        if (!Pattern.matches(Constants.RegexString.USERNAME_REGEX, bkavUserDto.getUsername())) {
            return false;
        }
        return true;
    }

    private static boolean validateInputUpdateUser(BkavUserDto bkavUserDto) {

        if (!StringUtil.isNullOrEmpty(bkavUserDto.getName())) {
            if (!Pattern.matches(Constants.RegexString.NAME_REGEX, bkavUserDto.getName())) {
                return false;
            }
        }

        if (!StringUtil.isNullOrEmpty(bkavUserDto.getUsername())) {
            if (!Pattern.matches(Constants.RegexString.USERNAME_REGEX, bkavUserDto.getUsername())) {
                return false;
            }
        }

        if (!StringUtil.isNullOrEmpty(bkavUserDto.getPassword())) {
            if (!Pattern.matches(Constants.RegexString.PASSWORD_REGEX, bkavUserDto.getPassword())) {
                return false;
            }
        }

        return true;
    }

}
