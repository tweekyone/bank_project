package com.epam.clientinterface.domain.exception;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(long cardId) {
        super(String.format("Card with id %s not found", cardId));
    }
}
