package com.epam.clientinterface.service.impl;

import com.epam.clientinterface.dto.UserDto;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.UserRepository;
import com.epam.clientinterface.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepo;

    private Account accountRepo;

    private List<Account> accounts = new ArrayList<>();

    @Override
    public User signUp(String name, String surname, String phoneNumber,
                       String username, String email, String rawPassword) {
        return new User();
    }

    @Override
    public User signUp(UserDto dto) {
        User newUser = new User("Ivan", "Popov", "+79100000",
            "vanok", "vanok@gmail.com", "1234", accounts);
        System.out.println(dto);
        return newUser;
    }

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
