package com.epam.clientinterface.service.impl;

import com.epam.clientinterface.domain.exception.UserAlreadyExistException;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.UserRepository;
import com.epam.clientinterface.service.AuthService;
import com.epam.clientinterface.service.UserService;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    // TODO - create Bean of user service implementation
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public User signUp(String name, String surname, String phoneNumber,
                       String username, String email, String rawPassword)
        throws UserAlreadyExistException {
        if (emailExist(email)) {
            throw new UserAlreadyExistException(email);
        }
        User newUser = userService.create(name, surname, phoneNumber, username, email, rawPassword);

        // Uncomment line below to use direct save in user repo instead of user service (only for check!)
        // List<Account> accounts = new ArrayList<>();
        // User newUser = new User(name, surname, phoneNumber, username, email, rawPassword, accounts);
        // userRepository.save(newUser);

        return newUser;
    }

    private boolean emailExist(String email) {
        return userRepository.existsByEmail(email);
    }
}
