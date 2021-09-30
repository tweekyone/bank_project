package com.epam.bank.atm.entity;

public class Card {
    private final long id;
    private final String number;
    private final long accountId;
    private final String pin;

    public Card(long id, String number, long accountId, String pin) {
        this.id = id;
        this.number = number;
        this.accountId = accountId;
        this.pin = pin;
    }

    public long getId() {
        return this.id;
    }

    public String getNumber() {
        return this.number;
    }

    public long getAccountId() {
        return this.accountId;
    }

    public String getPin() {
        return this.pin;
    }
}
