package com.epam.bank.atm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Account {

    private final long id;
    private double number;
    private boolean isDefault;
    private String plan;
    private double amount;
    private final long userId;
    private double amount;

    public Account(long id, long userId, double amount) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
    }

    public long getId() {
        return this.id;
    }

    public long getUserId() {
        return this.userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && userId == account.userId && Double.compare(account.amount, amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, amount);
    }

}
