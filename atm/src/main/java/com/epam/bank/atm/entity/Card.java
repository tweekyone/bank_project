package com.epam.bank.atm.entity;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Card card = (Card) o;
        return id == card.id && number == card.number && accountId == card.accountId && pinCode == card.pinCode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, accountId, pinCode);
    }
}
