package com.epam.bank.atm.service;

import com.epam.bank.atm.entity.Account;
import com.epam.bank.atm.repository.AccountRepository;
import java.math.BigInteger;

public class AccountService {

    private TransactionalService service;

    public AccountService() {
    }

    public double putMoney(long id, double amount) {
        checkMinimumAmountForOperation(amount);
        double result = repository.putMoney(id, amount);
        if (result != -1) {
            service.createTransaction(null, new BigInteger(
                String.valueOf(repository.getAccountNumberById(id))), amount, "refill");
        }
        return result;
    }

    public double withdrawMoney(long id, double amount) {
        if(repository.getCurrentAmount(id) < amount){
            throw new IllegalArgumentException("Less than the current balance");
        }
        checkMinimumAmountForOperation(amount);
        double result = repository.withdrawMoney(id, amount);
        if (result != -1) {
            service.createTransaction(new BigInteger(
                String.valueOf(repository.getAccountNumberById(id))), null, amount, "withdraw");
        }
        return result;
    }

    private void checkMinimumAmountForOperation(double amount) {
        if (amount < 0.01) {
            throw new IllegalArgumentException("Less than the minimum amount");
        }
    }

    private AccountRepository repository = new AccountRepository() {
        @Override
        public Account getById(long id) {
            return null;
        }

        @Override
        public double putMoney(long id, double amount) {
            return 5678.58 + amount;
        }

        @Override
        public double withdrawMoney(long id, double amount) {
            return 5678.58 - amount;
        }

        @Override
        public double getCurrentAmount(long id) {
            return 5678.58;
        }

        @Override
        public BigInteger getAccountNumberById(long id) {
            return new BigInteger("40817810099910004312");
        }
    };
}
