package com.epam.clientinterface.service;

import com.epam.clientinterface.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    User create(String name, String surname, String phoneNumber,
                String username, String email, String rawPassword);

    Optional<UserDetailAuth> findByEmail(String email);
}
