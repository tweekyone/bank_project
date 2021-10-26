package com.epam.clientinterface.service.impl;

import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.exception.UserAlreadyExistException;
import com.epam.clientinterface.repository.UserRepository;
import com.epam.clientinterface.service.AuthService;
import com.epam.clientinterface.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    // @Autowired? TODO - create Beans below like in my realization
    private UserService userService;
    private UserRepository userRepository;
    // Mock
    private List<Account> accounts = new ArrayList<>();

    @Override
    public User signUp(String name, String surname, String phoneNumber,
                       String username, String email, String rawPassword) throws UserAlreadyExistException {
        // if (emailExist(email)) {
        //     throw new UserAlreadyExistException(email);
        // }
        // User newUser = userService.create(name, surname, phoneNumber, username, email, rawPassword);
        // Mocked user, uncomment line below to use mock instead of userService
        User newUser = new User(name, surname, phoneNumber, username, email, rawPassword, accounts);
        System.out.println(newUser);
        return newUser;
    }

    private boolean emailExist(String email) {
        return !userRepository.findByEmail(email);
    }
}
