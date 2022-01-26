package com.epam.bank.operatorinterface.exception;

public class AccountIsNotSupposedForExternalTransferException extends RuntimeException {
    public AccountIsNotSupposedForExternalTransferException(String accountNumber) {
        super(String.format("Account of number=%s is not supposed for external transfer", accountNumber));
    }
}
