package com.epam.bank.operatorinterface.exception;

public class CardIsBlockedException extends RuntimeException {
    public CardIsBlockedException(long cardId) {
        super(String.format("Card of id=%d is blocked", cardId));
    }
}
