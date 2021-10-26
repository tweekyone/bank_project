package com.epam.clientinterface.service.impl;

import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.service.AuthService;
import com.epam.clientinterface.service.UserService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private UserService userService;

    private List<Account> accounts = new ArrayList<>();

    @Override
    public User signUp(String name, String surname, String phoneNumber,
                       String username, String email, String rawPassword) {
        // User newUser = userService.create(name, surname, phoneNumber, username, email, rawPassword);
        User newUser = new User(name, surname, phoneNumber, username, email, rawPassword, accounts);
        System.out.println(newUser);
        return newUser;
    }

    // public User signUp(UserDto dto) {
    //     // User newUser = new User("Ivan", "Popov", "+79100000",
    //     //     "vanok", "vanok@gmail.com", "1234", accounts);
    //     System.out.println(dto);
    //     return userService.create(dto.getName(), dto.getSurname(), dto.getPhoneNumber(),
    //         dto.getUsername(), dto.getEmail(), dto.getPassword());
    // }

    // @Override
    // public User registerNewUserAccount(UserDto userDto) throws UserAlreadyExistException {
    //     if (emailExist(userDto.getEmail())) {
    //         throw new UserAlreadyExistException("There is an account with that email address: "
    //             + userDto.getEmail());
    //     }
    //
    //     // the rest of the registration operation
    // }

    // private boolean emailExist(String email) {
    //     return userRepository.findByEmail(email) != null;
    // }
}
