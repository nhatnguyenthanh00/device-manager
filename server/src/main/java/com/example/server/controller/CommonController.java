package com.example.server.controller;

import com.example.server.resquest.LoginRequest;
import com.example.server.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
//@RequestMapping("/api")
public class CommonController {

    @Autowired
    AccountService accountService;
    private List<String> users = new ArrayList<>(List.of(
            "Nhat",
            "Chimmy",
            "Lucifer"
    ));
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest){
        return accountService.verify(loginRequest);
    }

    @GetMapping("/")
    public String test(HttpServletRequest request){
        return "Login sucess! " + request.getSession().getId();
    }

    @GetMapping("/user")
    public List<String> getUsers(){
        return users;
    }

    @PostMapping("/user")
    public String addUser(@RequestBody String user){
        users.add(user);
        return user;
    }
}
