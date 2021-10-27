package com.epam.clientinterface.service.impl;

import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.exception.UserAlreadyExistException;
import com.epam.clientinterface.repository.UserRepository;
import com.epam.clientinterface.service.AuthService;
import com.epam.clientinterface.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    // TODO - create Bean of user service implementation
    private UserService userService;
    // TODO -
    private UserRepository userRepository;

    @Override
    public User signUp(String name, String surname, String phoneNumber,
                       String username, String email, String rawPassword) throws UserAlreadyExistException {
        // if (emailExist(email)) {
        //     throw new UserAlreadyExistException(email);
        // }
        // User newUser = userService.create(name, surname, phoneNumber, username, email, rawPassword);
        // Mocked user, uncomment line below to use mock instead of userService
        List<Account> accounts = new ArrayList<>();
        User newUser = new User(name, surname, phoneNumber, username, email, rawPassword, accounts);
        System.out.println(newUser);
        return newUser;
    }

    private boolean emailExist(String email) {
        return !userRepository.findByEmail(email);
    }
}
