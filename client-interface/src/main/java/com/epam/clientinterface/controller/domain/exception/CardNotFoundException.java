package com.epam.clientinterface.controller.domain.exception;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(long cardId) {
        super(String.format("Card of id=%d not found", cardId));
    }
}
