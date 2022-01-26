package com.epam.bank.operatorinterface.exception;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(long cardId) {
        super(String.format("Card of id=%d not found", cardId));
    }

    public CardNotFoundException(String cardNumber) {
        super(String.format("Card of id=%s not found", cardNumber));
    }
}
