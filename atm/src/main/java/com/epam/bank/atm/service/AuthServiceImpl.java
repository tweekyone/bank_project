package com.epam.bank.atm.service;

import com.epam.bank.atm.controller.di.DIContainer;
import com.epam.bank.atm.domain.model.AuthDescriptor;
import com.epam.bank.atm.entity.Account;
import com.epam.bank.atm.entity.Card;
import com.epam.bank.atm.entity.User;
import com.epam.bank.atm.repository.AccountRepository;
import com.epam.bank.atm.repository.CardRepository;
import com.epam.bank.atm.repository.UserRepository;

public class AuthServiceImpl implements AuthService{
    private AccountRepository accountRepository;
    private CardRepository cardRepository;
    private UserRepository userRepository;

    public AuthServiceImpl() {
        this.accountRepository = DIContainer.instance().getSingleton(AccountRepository.class);
        this.cardRepository = DIContainer.instance().getSingleton(CardRepository.class);
        this.userRepository = DIContainer.instance().getSingleton(UserRepository.class);
    }

    @Override
    public AuthDescriptor login(String cardNumber, String pin) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            throw new IllegalArgumentException("Error! Card number is empty");
        } else if (pin == null || pin.isEmpty()) {
            throw new IllegalArgumentException("Error! Pin is empty");
        }

        Card card = cardRepository.getById(Long.parseLong(cardNumber));
        if (card == null) {
            throw new IllegalArgumentException("Error! Card number is incorrect");
        } else if (card.getPinCode() == Integer.parseInt(pin)) {
            Account account = accountRepository.getById(card.getAccountId());
            User user = userRepository.getById(account.getUserId());

            return new AuthDescriptor(user, account, card);
        } else {
            throw new IllegalArgumentException("Error! Pin code is incorrect");
        }
    }
}
