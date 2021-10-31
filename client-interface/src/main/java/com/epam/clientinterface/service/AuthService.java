package com.epam.clientinterface.service;

import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.exception.UserAlreadyExistException;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    User signUp (String name, String surname, String phoneNumber,
                String username, String email, String rawPassword) throws UserAlreadyExistException;

    // User signUp(UserDto dto);
}
