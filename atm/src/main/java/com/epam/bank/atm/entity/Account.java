package com.epam.bank.atm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Account {

    private long id;
    private double number;
    private boolean isDefault;
    private String plan;
    private double amount;
    private final long userId;

    public Account(double number, boolean isDefault, String plan, double amount, long userId) {
        this.number = number;
        this.isDefault = isDefault;
        this.plan = plan;
        this.amount = amount;
        this.userId = userId;
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
