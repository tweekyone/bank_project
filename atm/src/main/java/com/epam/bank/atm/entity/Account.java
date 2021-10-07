package com.epam.bank.atm.entity;

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
}
