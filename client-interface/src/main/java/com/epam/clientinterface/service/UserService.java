package com.epam.clientinterface.service;

import com.epam.clientinterface.domain.UserDetailAuth;
import com.epam.clientinterface.entity.User;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User create(String name, String surname, String phoneNumber,
                String username, String email, String rawPassword);

    Optional<UserDetailAuth> findByEmail(String email);
}
