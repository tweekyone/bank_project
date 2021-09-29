package com.epam.bank.atm.service;

import java.math.BigInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AccountServiceTest {

    private double currentAmount = 5678.58;
    private double positiveAmount = 578.58;
    private double negativeAmount = -578.58;
    private double zeroAmount = 0.00;
    private BigInteger correctId = new BigInteger("40817810099910004312");
    private BigInteger notCorrectId = new BigInteger("40817910004312");

    private AccountService service = new AccountService(new AccountRepository());

    public AccountServiceTest() {
    }

    @Test
    void putMoney() {
        Assertions.assertEquals(currentAmount + positiveAmount, service.putMoney(correctId, positiveAmount));
    }

    @Test
    void putMoneyZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.putMoney(correctId, zeroAmount));
    }

    @Test
    void putMoneyIncorrectIdAndAmount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.putMoney(correctId, negativeAmount));
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.putMoney(notCorrectId, positiveAmount));
    }

    @Test
    void withdrawMoney() {
        Assertions.assertEquals(currentAmount - positiveAmount, service.withdrawMoney(correctId, positiveAmount));
    }

    @Test
    void withdrawMoneyIncorrectIdAndAmount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.withdrawMoney(correctId, negativeAmount));
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> service.withdrawMoney(notCorrectId, positiveAmount));
    }

    @Test
    void withdrawMoneyMoneyZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.withdrawMoney(correctId, zeroAmount));
    }
}