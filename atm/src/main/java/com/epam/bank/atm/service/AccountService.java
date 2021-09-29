package com.epam.bank.atm.service;

import java.math.BigInteger;

public class AccountService {

    private AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public double putMoney(BigInteger id, double amount) {
        checkIdAndAmount(id, amount);
        return repository.putMoney(id, amount);
    }

    public double withdrawMoney(BigInteger id, double amount) {
        checkIdAndAmount(id, amount);
        return repository.withdrawMoney(id, amount);
    }

    private void checkIdAndAmount(BigInteger id, double amount) {
        if (getDigitCount(id) != 20) {
            throw new IllegalArgumentException("Wrong account number");
        }
        if (amount < 0.01) {
            throw new IllegalArgumentException("Less than the minimum amount");
        }
    }

    private int getDigitCount(BigInteger number) {
        double factor = Math.log(2) / Math.log(10);
        int digitCount = (int) (factor * number.bitLength() + 1);
        if (BigInteger.TEN.pow(digitCount - 1).compareTo(number) > 0) {
            return digitCount - 1;
        }
        return digitCount;
    }
}

class AccountRepository {
//mock
    public double putMoney(BigInteger id, double amount) {
        //double currentSum = "Select * from account where id=:?";
        //String s = "UPDATE account SET amount=newAmount where id=:id";
        return 5678.58 + amount;
    }

    public double withdrawMoney(BigInteger id, double amount) {
        return 5678.58 - amount;
    }
}
