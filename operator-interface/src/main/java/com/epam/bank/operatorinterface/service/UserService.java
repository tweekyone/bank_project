package com.epam.bank.operatorinterface.service;

import com.epam.bank.operatorinterface.entity.AccountPlan;
import com.epam.bank.operatorinterface.entity.User;
import com.epam.bank.operatorinterface.repository.UserRepository;
import com.epam.bank.operatorinterface.service.dto.CreateUserDto;
import com.epam.bank.operatorinterface.util.ValidationFacade;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ValidationFacade validationFacade;
    private final AccountService accountService;

    public User create(String name, String surname, String phone, String username, String email, String rawPassword) {
        validationFacade.validateOrThrow(new CreateUserDto(name, surname, phone, username, email, rawPassword));

        // ToDo: encrypt raw password when spring security is ready
        var user = new User(name, surname, phone, username, email, rawPassword);

        return userRepository.save(user);
    }

    @Transactional
    public User createClient(
        String name, String surname, String phone, String username, String email, String rawPassword
    ) {
        var user = create(name, surname, phone, username, email, rawPassword);
        accountService.create(user.getId(), AccountPlan.BASE);

        return user;
    }
}
