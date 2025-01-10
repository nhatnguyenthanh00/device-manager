package com.example.server.service.impl;

import com.example.server.dao.JpaBkavUserDao;
import com.example.server.dto.DeviceDto;
import com.example.server.dto.UserDto;
import com.example.server.model.BkavUser;
import com.example.server.model.UserPrincipal;
import com.example.server.model.view.BkavUserDeviceView;
import com.example.server.repository.view.BkavUserDeviceViewRepository;
import com.example.server.repository.UserRepository;
import com.example.server.resquest.LoginRequest;
import com.example.server.config.JwtService;
import com.example.server.service.iService.IUserServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements IUserServiceService {

    @Autowired
    JpaBkavUserDao jpaBkavUserDao;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BkavUserDeviceViewRepository bkavUserDeviceViewRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);

    @Override
    public List<?> getAllUser(String gender, String search) {
        List<BkavUserDeviceView> viewData = jpaBkavUserDao.getUserByGenderAndName(gender,search);
        Map<UUID, UserDto> groupedData = new HashMap<>();

        for (BkavUserDeviceView record : viewData) {
            UUID userId = record.getUserId();

            // Check if the user already exists in the map
            UserDto userDeviceDTO = groupedData.computeIfAbsent(userId, id -> {
                UserDto newUserDeviceDTO = new UserDto();
                newUserDeviceDTO.setUserId(record.getUserId());
                newUserDeviceDTO.setName(record.getName());
                newUserDeviceDTO.setUsername(record.getUsername());
                newUserDeviceDTO.setRole(record.getRole());
                newUserDeviceDTO.setGender(record.getGender());
                newUserDeviceDTO.setDevices(new ArrayList<>());
                return newUserDeviceDTO;
            });

            // Add device to the user's device list (only if it's not already there)
            if (record.getDeviceId() != null) { // Skip if device is NULL
                boolean deviceExists = userDeviceDTO.getDevices().stream()
                        .anyMatch(device -> device.getDeviceId().equals(record.getDeviceId()));

                if (!deviceExists) { // Add device if it's not already in the list
                    DeviceDto deviceDTO = new DeviceDto();
                    deviceDTO.setDeviceId(record.getDeviceId());
                    deviceDTO.setDeviceName(record.getDeviceName());
                    deviceDTO.setDeviceDescription(record.getDeviceDescription());
                    deviceDTO.setDeviceCategory(record.getDeviceCategory());
                    deviceDTO.setDeviceStatus(record.getDeviceStatus());
                    userDeviceDTO.getDevices().add(deviceDTO);
                }
            }
        }

        return new ArrayList<>(groupedData.values());
//        return viewData;
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
    public BkavUser save(BkavUser bkavUser) {
        bkavUser.setPassword(encoder.encode(bkavUser.getPassword()));
        return userRepository.save(bkavUser);
    }

    @Override
    public void delete(BkavUser bkavUser) {
        return;
    }
}
