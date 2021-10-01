package com.epam.bank.atm.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AccountServiceTest {

    private final double currentAmount = 5678.58;
    private final double positiveAmount = 578.58;
    private final double negativeAmount = -578.58;
    private final double zeroAmount = 0.00;
    private final long accountId = 40817810;

    private final AccountService service = new AccountService(new AccountRepository());

    public AccountServiceTest() {
    }

    @Test
    void putMoney() {
        Assertions.assertEquals(currentAmount + positiveAmount, service.putMoney(accountId, positiveAmount));
    }

    @Test
    void putMoneyZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.putMoney(accountId, zeroAmount));
    }

    @Test
    void putMoneyIncorrectAmount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.putMoney(accountId, negativeAmount));
    }

    @Test
    void withdrawMoney() {
        Assertions.assertEquals(currentAmount - positiveAmount, service.withdrawMoney(accountId, positiveAmount));
    }

    @Test
    void withdrawMoneyIncorrectAmount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.withdrawMoney(accountId, negativeAmount));
    }

    @Test
    void withdrawMoneyMoneyZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.withdrawMoney(accountId, zeroAmount));
    }
}