package com.epam.bank.operatorinterface.exception;

public class InvalidPinCodeFormatException extends RuntimeException {
    public InvalidPinCodeFormatException() {
        super("Invalid pin code format");
    }
}
