package com.example.server.config.security;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.entity.BkavUser;
import com.example.server.repository.dao.idao.BkavUserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {


    @Autowired
    private BkavUserDao bkavUserDao;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        BkavUser bkavUser = bkavUserDao.findByUserName(username);

        if(bkavUser == null){
            throw new UsernameNotFoundException("User not found");
        }

        return new UserPrincipal(bkavUser);
    }
}
