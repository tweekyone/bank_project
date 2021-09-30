package com.epam.bank.atm.service;

import com.epam.bank.atm.domain.model.AuthDescriptor;
import com.epam.bank.atm.repository.AccountRepository;
import com.epam.bank.atm.repository.CardRepository;
import com.epam.bank.atm.repository.UserRepository;

public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;

    public AuthServiceImpl(
        UserRepository userRepository,
        AccountRepository accountRepository,
        CardRepository cardRepository
    ) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
    }

    @Override
    public AuthDescriptor login(String cardNumber, String pin) {
        if (cardNumber == null) {
            throw new RuntimeException("Card number is empty");
        }

        if (pin == null) {
            throw new RuntimeException("Card number is empty");
        }

        var card = this.cardRepository.getByNumber(cardNumber);

        if (card == null) {
            throw new RuntimeException("Card not found");
        }

        if (!card.getPin().equals(pin)) {
            throw new RuntimeException("Pin is incorrect");
        }

        var account = this.accountRepository.getById(card.getAccountId());

        if (account == null) {
            throw new RuntimeException("Account not found");
        }

        var user = this.userRepository.getById(account.getUserId());

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return new AuthDescriptor(user, account, card);
    }
}
