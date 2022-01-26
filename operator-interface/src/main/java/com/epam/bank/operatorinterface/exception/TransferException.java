package com.epam.bank.operatorinterface.exception;

public class TransferException extends RuntimeException {
    public TransferException() {
        super(String.format("Transfer is not available for the same account"));
    }
}
