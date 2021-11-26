package com.epam.clientinterface.domain;

import com.epam.clientinterface.entity.User;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

public class UserDetailAuth extends User implements UserDetails {

    public UserDetailAuth() {
    }

    public UserDetailAuth(User user) {
        super(user.getId(), user.getName(), user.getSurname(), user.getPhoneNumber(), user.getEmail(),
            user.getPassword(), user.isEnabled(), user.getFailedLoginAttempts(), user.getRoles());
    }

    @Override
    @Transactional
    public Set<RoleAuth> getAuthorities() {
        return super.getRoles().stream()
            .map(role -> new RoleAuth(role.getId(), role.getAuthority()))
            .collect(Collectors.toSet());
    }

    @Override
    public String getUsername() {
        return super.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return super.isEnabled();
    }

    @Override
    public boolean isAccountNonLocked() {
        return super.isEnabled();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return super.isEnabled();
    }
}
