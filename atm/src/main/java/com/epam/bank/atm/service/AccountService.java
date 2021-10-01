package com.epam.bank.atm.service;

public class AccountService {

    private AccountRepository repository;

    public AccountService(AccountRepository repository) {
        this.repository = repository;
    }

    public double putMoney(long id, double amount) {
        checkAmount(amount);
        return repository.putMoney(id, amount);
    }

    public double withdrawMoney(long id, double amount) {
        checkAmount(amount);
        return repository.withdrawMoney(id, amount);
    }

    private void checkAmount(double amount) {
        if (amount < 0.01) {
            throw new IllegalArgumentException("Less than the minimum amount");
        }
    }
}

class AccountRepository {
    //mock
    public double putMoney(long id, double amount) {
        //double currentSum = "Select * from account where id=:?";
        //String s = "UPDATE account SET amount=newAmount where id=:id";
        return 5678.58 + amount;
    }

    public double withdrawMoney(long id, double amount) {
        return 5678.58 - amount;
    }
}
