package com.epam.bank.atm.entity;

public class Card {
    private final long id;
    private final long number;
    private final long accountId;
    private final int pinCode;

    public Card(long id, long number, long accountId, int pinCode) {
        this.id = id;
        this.number = number;
        this.accountId = accountId;
        this.pinCode = pinCode;
    }

    public long getId() {
        return this.id;
    }

    public long getNumber() {
        return this.number;
    }

    public long getAccountId() {
        return this.accountId;
    }

    public int getPinCode() {
        return this.pinCode;
    }
}
