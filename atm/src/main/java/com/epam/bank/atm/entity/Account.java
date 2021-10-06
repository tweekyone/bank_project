package com.epam.bank.atm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Account {

    private final long id;
    private double number;
    private boolean isDefault;
    private String plan;
    private double amount;
    private final long userId;

    public Account(long id, long userId) {
        this.id = id;
        this.userId = userId;
    }
}
