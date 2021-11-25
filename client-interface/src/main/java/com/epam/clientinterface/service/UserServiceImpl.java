package com.epam.clientinterface.service;

import com.epam.clientinterface.domain.UserDetailAuth;
import com.epam.clientinterface.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        UserDetailAuth userDetailAuth = findByEmail(userEmail)
            .orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with userEmail - %s not found", userEmail)));

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(userEmail)
            .password(userDetailAuth.getPassword())
            .disabled(!userDetailAuth.isEnabled())
            .authorities(userDetailAuth.getAuthorities())
            .build();
        return userDetails;
    }

    @Transactional
    public Optional<UserDetailAuth> findByEmail(String email) {
        return repository.findByEmailWithRoles(email).map(UserDetailAuth::new);
    }

    //TODO add encoding and setAuthorities into create user
}
