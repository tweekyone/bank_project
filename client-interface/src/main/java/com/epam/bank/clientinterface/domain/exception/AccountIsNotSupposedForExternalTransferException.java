package com.epam.bank.clientinterface.domain.exception;

public class AccountIsNotSupposedForExternalTransferException extends RuntimeException {
    public AccountIsNotSupposedForExternalTransferException(long accountId) {
        super(String.format("Account of id=%d is not supposed for external transfer", accountId));
    }
}
