package com.epam.bank.atm.entity;

public class Card {
    private final long id;
    private final long accountId;

    public Card(long id, long accountId) {
        this.id = id;
        this.accountId = accountId;
    }

    public long getId() {
        return this.id;
    }

    public long getAccountId() {
        return this.accountId;
    }
}
