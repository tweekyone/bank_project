package com.epam.bank.atm.service;

import com.epam.bank.atm.entity.Transaction;
import com.epam.bank.atm.repository.AccountRepository;

public class AccountService {

    private final TransactionalService service;
    private final AccountRepository repository;

    public AccountService(TransactionalService service, AccountRepository repository) {
        this.service = service;
        this.repository = repository;
    }

    public double putMoney(long id, double amount) {
        checkMinimumAmountForOperation(amount);
        double currentAmount = repository.getCurrentAmount(id);
        double result = repository.putMoney(id, amount);
        if (result <= currentAmount) {
            service.createTransaction(null, id, amount,
                Transaction.OperationType.CASH,
                Transaction.State.CANCELLED);
            throw new IllegalStateException("Funds have not been credited to the account");
        }
        service.createTransaction(null, id, amount,
            Transaction.OperationType.CASH,
            Transaction.State.DONE);
        return result;
    }

    public double withdrawMoney(long id, double amount) {
        double currentAmount = repository.getCurrentAmount(id);
        if (currentAmount < amount) {
            throw new IllegalArgumentException("More than the current balance");
        }
        checkMinimumAmountForOperation(amount);
        double result = repository.withdrawMoney(id, amount);
        if (result >= currentAmount) {
            service.createTransaction(id, null, amount,
                Transaction.OperationType.WITHDRAWAL,
                Transaction.State.CANCELLED);
            throw new IllegalStateException("Funds have not been withdrawn from the account");
        }
        service.createTransaction(id, null, amount,
            Transaction.OperationType.CASH, Transaction.State.DONE);

        return result;
    }

    private void checkMinimumAmountForOperation(double amount) {
        if (amount < 0.01) {
            throw new IllegalArgumentException("Less than the minimum amount");
        }
    }
}
