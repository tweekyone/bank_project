package com.epam.clientinterface.service.impl;

import com.epam.clientinterface.domain.exception.UserAlreadyExistException;
import com.epam.clientinterface.domain.exception.UsernameAlreadyTakenException;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.UserRepository;
import com.epam.clientinterface.service.AuthService;
import com.epam.clientinterface.service.UserService;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    private final UserRepository userRepository;

    @Override
    public User signUp(String name, String surname, String phoneNumber,
                       String username, String email, String rawPassword)
        throws UserAlreadyExistException {
        if (isEmailExist(email)) {
            throw new UserAlreadyExistException(email);
        }
        if (isUsernameExist(username)) {
            throw new UsernameAlreadyTakenException(username);
        }

        return userService.create(name, surname, phoneNumber, username, email, rawPassword);
    }

    private boolean isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    private boolean isUsernameExist(String username) {
        return userRepository.existsByUsername(username);
    }
}
