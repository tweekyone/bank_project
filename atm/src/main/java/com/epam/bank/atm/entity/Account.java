package com.epam.bank.atm.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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

}
