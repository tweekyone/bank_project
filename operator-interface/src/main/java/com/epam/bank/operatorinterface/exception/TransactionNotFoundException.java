package com.epam.bank.operatorinterface.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(long accountId) {
        super(String.format("Transaction with id=%d not found", accountId));
    }

    public TransactionNotFoundException(String accountNumber) {
        super(String.format("Transactions with account number=%s not found", accountNumber));
    }
}
