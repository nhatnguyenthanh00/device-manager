package com.example.server.service.impl;

import ch.qos.logback.core.util.StringUtil;
import com.example.server.config.security.UserPrincipal;
import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.dto.SelectUser;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.model.response.DetailUserResponse;
import com.example.server.model.response.ErrorResponse;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> changePassWord(String username, String oldPassword, String newPassword) {
        BkavUserDto user = bkavUserDao.findByUserName(username);
        if (newPassword.equals(oldPassword))
            return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.NEW_PASSWORD_SAME_OLD_PASSWORD), HttpStatus.BAD_REQUEST);
        if (!encoder.matches(oldPassword, user.getPassword())) {
            return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.INCORRECT_PASSWORD), HttpStatus.BAD_REQUEST);
        }
        String encodeNewPassword = encoder.encode(newPassword);
        user.setPassword(encodeNewPassword);
        bkavUserDao.save(user);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> resetPassword(ResetPasswordRequest request) {

//        try {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserPrincipal userPrincipal = (UserPrincipal) principal;
        if (!encoder.matches(request.getAdminPassword(), userPrincipal.getPassword())) {
            return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.ADMIN_PASSWORD_INCORRECT), HttpStatus.BAD_REQUEST);
        }
        BkavUserDto userDto = new BkavUserDto();
        userDto.setUserId(request.getUserId());
        userDto.setPassword(request.getPassword());
        save(userDto);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> findDetailUser(String userId, int page) {

        UUID id = UUID.fromString(userId);
        BkavUserDto userDto = findById(id);
        if (userDto == null) {
            return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.NOT_FOUND_USER), HttpStatus.BAD_REQUEST);
//                return new SampleResponse<>(null, Constants.ErrorMessage.NOT_FOUND_USER);
        }
        PageView<DeviceInfoView> pageView = deviceDao.findAllDeviceByUsername(userDto.getUsername(), page);
        return new ResponseEntity<>(new DetailUserResponse(userDto, pageView), HttpStatus.OK);
//            return new SampleResponse<>(new DetailUserResponse(userDto, pageView));

    }

    @Override
    public List<SelectUser> findAllSelectUser() {
        return bkavUserDao.findAllSelectUser();
    }

    @Override
    public ResponseEntity<?> save(BkavUserDto userDto) {

        UUID userId = userDto.getUserId();
        // case create
        if (userId == null) {
            if (!validateInputCreateUser(userDto)) {
                return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.INVALID_INPUT),HttpStatus.BAD_REQUEST);
//                return new SampleResponse<>(null, Constants.ErrorMessage.INVALID_INPUT);
            }
            BkavUserDto findUser = bkavUserDao.findByUserName(userDto.getUsername());
            if (findUser != null) {
                return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.USERNAME_EXISTED),HttpStatus.BAD_REQUEST);
//                return new SampleResponse<>(null, Constants.ErrorMessage.USERNAME_EXISTED);
            }
            String password = userDto.getPassword();
            userDto.setPassword(encoder.encode(password));
            userDto.setRole(Role.USER);
            return new ResponseEntity<>(bkavUserDao.save(userDto),HttpStatus.OK);
//            return new SampleResponse<>(bkavUserDao.save(userDto));
        }
        // case update
        else {
            if (!validateInputUpdateUser(userDto)) {
                return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.INVALID_INPUT),HttpStatus.BAD_REQUEST);
//                return new SampleResponse<>(null, Constants.ErrorMessage.INVALID_INPUT);

            }
            BkavUserDto findUser = bkavUserDao.findById(userId).orElse(null);
            if (findUser == null) {
                return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.NOT_FOUND_USER),HttpStatus.BAD_REQUEST);
//                return new SampleResponse<>(null, Constants.ErrorMessage.NOT_FOUND_USER);
            }
            String username = userDto.getUsername();

            if (username != null && !username.equals(findUser.getUsername())) {
                BkavUserDto findExistUser = bkavUserDao.findByUserName(username);
                if (findExistUser != null) {
                    return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.USERNAME_EXISTED),HttpStatus.BAD_REQUEST);
//                    return new SampleResponse<>(null, Constants.ErrorMessage.USERNAME_EXISTED);
                }
            }
            if (username != null) findUser.setUsername(userDto.getUsername());
            String name = userDto.getName();
            String password = userDto.getPassword();
            Role role = userDto.getRole();
            Gender gender = userDto.getGender();
            if (!StringUtil.isNullOrEmpty(name)) findUser.setName(name);
            if (!StringUtil.isNullOrEmpty(password)) findUser.setPassword(encoder.encode(password));
            if (role != null) findUser.setRole(role);
            if (gender != null) findUser.setGender(gender);

            return new ResponseEntity<>(bkavUserDao.save(findUser),HttpStatus.OK);
//            return new SampleResponse<>(bkavUserDao.save(findUser));

        }
    }

    @Override
    public ResponseEntity<?> deleteById(UUID id) {
        BkavUserDto findUser = bkavUserDao.findById(id).orElse(null);
        if (findUser == null) {
            return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.NOT_FOUND_USER), HttpStatus.BAD_REQUEST);
//                return new SampleResponse<>(false, Constants.ErrorMessage.NOT_FOUND_USER);
        }
        if (findUser.getRole().name().equals("USER")) {
            if (deviceInfoViewRepository.countDeviceInfoViewByUsername(findUser.getUsername()) > 0) {
                return new ResponseEntity<>(new ErrorResponse(Constants.ErrorMessage.USER_NOW_CONTROL_DEVICES), HttpStatus.BAD_REQUEST);
//                    return new SampleResponse<>(false, Constants.ErrorMessage.USER_NOW_CONTROL_DEVICES);
            }
            bkavUserDao.deleteById(id);
            return new ResponseEntity<>(null, HttpStatus.OK);
//                return new SampleResponse<>(true);
        } else {
            return new ResponseEntity<>(new ErrorResponse("Can't delete admin"), HttpStatus.BAD_REQUEST);
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
