package com.epam.bank.operatorinterface.exception;

public class AccountIsNotSupposedForWithdrawException extends RuntimeException {
    public AccountIsNotSupposedForWithdrawException(long accountId) {
        super(String.format("Account of id=%d is not supposed for withdraw", accountId));
    }
}
