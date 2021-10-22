package com.epam.clientinterface.controller;

import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.AccountRepository;
import com.epam.clientinterface.repository.CardRepository;
import com.epam.clientinterface.repository.UserRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// ToDo: test controller. Remove it.
@RestController
@RequiredArgsConstructor
public class TestController {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;

    @GetMapping("/test")
    public String test() {
        var user = new User(
            "name",
            "surname",
            "34567345",
            "username",
            "name.surname@gmail.com",
            "password",
            new Account.AsFirstFactory("11111111111111111111")
        );

        user = this.userRepository.save(user);

        var account = this.accountRepository.findById(user.getAccounts().get(0).getId()).orElseThrow();

        this.cardRepository.save(new Card(account, "1111111111111111", "1234", Card.Plan.BASE, LocalDateTime.now()));

        return "success";
    }
}
