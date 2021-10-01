package com.epam.bank.atm.entity;

public class Card {
    private final long id;
    private final String number;
    private final long accountId;
    private final String pinCode;

    public Card(long id, String number, long accountId, String pinCode) {
        this.id = id;
        this.number = number;
        this.accountId = accountId;
        this.pinCode = pinCode;
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

    public String getPinCode() {
        return this.pinCode;
    }
}
