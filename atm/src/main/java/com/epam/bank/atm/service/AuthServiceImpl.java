package com.epam.bank.atm.service;

import com.epam.bank.atm.domain.model.AuthDescriptor;
import com.epam.bank.atm.entity.Account;
import com.epam.bank.atm.entity.User;
import com.epam.bank.atm.repository.AccountRepository;
import com.epam.bank.atm.repository.CardRepository;
import com.epam.bank.atm.repository.UserRepository;

public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private AccountRepository accountRepository;
    private CardRepository cardRepository;

    public AuthServiceImpl(UserRepository userRepository, AccountRepository accountRepository,
                           CardRepository cardRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
    }

    @Override
    public AuthDescriptor login(String cardNumber, String pin) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            throw new IllegalArgumentException("Error! Card number is empty");
        } else if (pin == null || pin.isEmpty()) {
            throw new IllegalArgumentException("Error! Pin is empty");
        }

        var card = cardRepository.getByNumber(cardNumber);
        if (card.isEmpty()) {
            throw new IllegalArgumentException("Error! Card number is incorrect");
        } else if (card.get().getPinCode().equals(pin)) {
            Account account = accountRepository.getById(card.get().getAccountId());
            User user = userRepository.getById(account.getUserId());

            return new AuthDescriptor(user, account, card.get());
        } else {
            throw new IllegalArgumentException("Error! Pin code is incorrect");
        }
    }
}
