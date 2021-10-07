package com.epam.bank.atm.entity;

public class User {
    private final long id;

    public User(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }
}
