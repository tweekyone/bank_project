package com.epam.clientinterface.service.impl;

import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.AccountRepository;
import com.epam.clientinterface.repository.UserRepository;
import com.epam.clientinterface.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Override
    public User create(String name, String surname, String phoneNumber,
                       String username, String email, String rawPassword) {

        // Creating account for new user
        List<Account> accounts = new ArrayList<>();
        Account.AsFirstFactory firstFactory = new Account.AsFirstFactory(RandomStringUtils.randomNumeric(19));

        // Creating new user with account above
        User newUser = new User(name, surname, phoneNumber, username, email, rawPassword, accounts);
        Account newAccount = firstFactory.createFor(newUser);
        accounts.add(newAccount);

        // saving user and account to database
        if (newAccount != null) {
            userRepository.save(newUser);
            accountRepository.save(newAccount);
        }

        return newUser;
    }
}
