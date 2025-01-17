package com.example.server.config.security;

import com.example.server.model.dto.BkavUserDto;
import com.example.server.model.entity.BkavUser;
import com.example.server.utils.constants.Constants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class UserPrincipal implements UserDetails {

    private BkavUser bkavUser;
    public UserPrincipal(BkavUser bkavUser){
        this.bkavUser = bkavUser;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(Constants.Security.ROLE_UNDER + bkavUser.getRole().name()));
    }

    @Override
    public String getPassword() {
        return bkavUser.getPassword();
    }

    @Override
    public String getUsername() {
        return bkavUser.getUsername();
    }
}
