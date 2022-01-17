package com.epam.bank.operatorinterface.exception;

public class TooManyPinCodeChangesPerDayException extends RuntimeException {
    public TooManyPinCodeChangesPerDayException(long cardId) {
        super(String.format("Too many pin code changes for card of id=%d", cardId));
    }
}
