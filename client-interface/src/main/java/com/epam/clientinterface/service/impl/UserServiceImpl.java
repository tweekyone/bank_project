package com.epam.clientinterface.service.impl;

import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.UserRepository;
import com.epam.clientinterface.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User create(String name, String surname, String phoneNumber,
                       String username, String email, String rawPassword) {

        // Creating bank account for new user with default values and number consists of 20 random integers
        Account.AsFirstFactory firstFactory = new Account.AsFirstFactory(RandomStringUtils.randomNumeric(20));

        // Creating new user with account above
        User newUser = new User(name, surname, phoneNumber, username, email, rawPassword, firstFactory);

        // saving user and account to database
        userRepository.save(newUser);

        return newUser;
    }
}
