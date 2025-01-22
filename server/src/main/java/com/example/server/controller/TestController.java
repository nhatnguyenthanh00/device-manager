package com.example.server.controller;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.entity.view.DeviceInfoView;
import com.example.server.model.response.DetailUserResponse;
import com.example.server.model.response.PageResponse;
import com.example.server.model.response.SampleResponse;
import com.example.server.model.resquest.DetailUserRequest;
import com.example.server.repository.dao.idao.DeviceDao;
import com.example.server.repository.view.DeviceInfoViewRepository;
import com.example.server.service.iservice.UserService;
import com.example.server.utils.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    UserService userService;

    @Autowired
    DeviceDao deviceDao;
    @Autowired
    DeviceInfoViewRepository deviceInfoViewRepository;

    @GetMapping(Constants.ApiEndpoint.EMPTY_PATH)
    public String test(){
        return "WELCOME TO LUCIFER";
    }
    @PostMapping("/v1")
    public SampleResponse<DetailUserResponse> getDetailUser(@RequestBody DetailUserRequest request){
//        throw new RuntimeException();
        try{
            UUID id = UUID.fromString(request.getUserId());
            Integer page = request.getPage();
            BkavUserDto data = userService.getById(id);
            if(data == null) return new SampleResponse<>(null,"Not found user");
            Pageable pageable = PageRequest.of(page-1,Constants.Common.NUMBER_5_INT, Sort.by("name").ascending());
            Page<DeviceInfoView> infoViewPage = deviceInfoViewRepository.findDeviceInfoViewByUsername(data.getUsername(),pageable);
            PageResponse<DeviceInfoView> pageResponse = deviceDao.getAllDeviceByUsername(data.getUsername(),page);
            return new SampleResponse<>(new DetailUserResponse(data,pageResponse));
        } catch (Exception e){
            return new SampleResponse<>(null,"Bad request");
        }
    }
}
