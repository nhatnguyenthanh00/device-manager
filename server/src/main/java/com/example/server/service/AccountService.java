package com.example.server.service;

import com.example.server.model.BkavUser;
import com.example.server.model.UserPrincipal;
import com.example.server.repository.AccountRepository;
import com.example.server.resquest.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(11);

    public BkavUser saveAccount(BkavUser bkavUser){
        bkavUser.setPassword(encoder.encode(bkavUser.getPassword()));
        return accountRepository.save(bkavUser);
    }

    public List<BkavUser> getAllAccounts(){
        return accountRepository.findAll();
    }

    public String verify(LoginRequest request){
        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        if (authentication.isAuthenticated()){
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return jwtService.generateToken(userPrincipal);
        }
        return null;
    }

}
