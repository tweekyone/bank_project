package com.epam.bank.atm.entity;

import java.util.Objects;

public class Account {
    private final long id;
    private final long userId;

    public Account(long id, long userId) {
        this.id = id;
        this.userId = userId;
    }

    public long getId() {
        return this.id;
    }

    public long getUserId() {
        return this.userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Account account = (Account) o;
        return id == account.id && userId == account.userId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}
