package com.example.server.model;

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
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + bkavUser.getRole().name()));
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
