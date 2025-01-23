package com.example.server.controller;

import com.example.server.repository.BkavUserRepository;
import com.example.server.repository.dao.idao.DeviceDao;
import com.example.server.repository.view.DeviceInfoViewRepository;
import com.example.server.service.iservice.UserService;
import com.example.server.utils.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    UserService userService;

    @Autowired
    DeviceDao deviceDao;
    @Autowired
    DeviceInfoViewRepository deviceInfoViewRepository;
    @Autowired
    BkavUserRepository bkavUserRepository;

    @GetMapping("/v1")
        public String test(@RequestParam(defaultValue = "") Integer page){
        return "WELCOME TO LUCIFER "+ page;
    }

}
