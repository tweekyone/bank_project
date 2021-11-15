package com.epam.clientinterface.service;

import com.epam.clientinterface.domain.exception.UserAlreadyExistException;
import com.epam.clientinterface.entity.User;

public interface AuthService {
    User signUp(String name, String surname, String phoneNumber,
                String username, String email, String rawPassword) throws UserAlreadyExistException;
}
