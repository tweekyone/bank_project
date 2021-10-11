package com.epam.bank.atm.service;

import com.epam.bank.atm.di.DIContainer;
import com.epam.bank.atm.entity.Transaction;
import com.epam.bank.atm.repository.AccountRepository;

public class AccountService {

    private final TransactionalService service;
    private final AccountRepository repository;

    public AccountService() {
        service = DIContainer.instance().getSingleton(TransactionalService.class);
        repository = DIContainer.instance().getSingleton(AccountRepository.class);
    }

    public double putMoney(long id, double amount) {
        checkMinimumAmountForOperation(amount);
        double result = repository.putMoney(id, amount);
        if (result != -1) {
            service.createTransaction(
                null,
                id,
                amount,
                Transaction.OperationType.CASH,
                Transaction.State.DONE
            );
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
            service.createTransaction(id, null, amount, Transaction.OperationType.CASH, Transaction.State.DONE);
        }
        return result;
    }

    private void checkMinimumAmountForOperation(double amount) {
        if (amount < 0.01) {
            throw new IllegalArgumentException("Less than the minimum amount");
        }
    }
}
