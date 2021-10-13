package com.epam.bank.atm.domain.model;

import com.epam.bank.atm.entity.Account;
import com.epam.bank.atm.entity.Card;
import com.epam.bank.atm.entity.User;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthDescriptor that = (AuthDescriptor) o;
        return user.equals(that.user) && account.equals(that.account) && card.equals(that.card);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, account, card);
    }
}
