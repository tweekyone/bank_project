package com.epam.bank.atm.domain.model;

import com.epam.bank.atm.entity.Account;
import com.epam.bank.atm.entity.Card;
import com.epam.bank.atm.entity.User;

public class AuthDescriptor {
    private final User user;
    private final Account account;
    private final Card card;

    public AuthDescriptor(User user, Account account, Card card) {
        this.user = user;
        this.account = account;
        this.card = card;
    }

    public User getUser() {
        return this.user;
    }

    public Account getAccount() {
        return this.account;
    }

    public Card getCard() {
        return this.card;
    }
}
