package com.epam.clientinterface.service;

import com.epam.clientinterface.domain.UserDetailAuth;
import java.util.Optional;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    Optional<UserDetailAuth> findByEmail(String email);
}
