package com.epam.bank.atm.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class Account {
    private long id;
    private String number;
    private boolean isDefault;
    private String plan;
    private double amount;
    private long userId;
    private LocalDateTime closedAt;
}
