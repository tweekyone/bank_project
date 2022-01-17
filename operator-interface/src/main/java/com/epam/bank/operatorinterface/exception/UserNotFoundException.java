package com.epam.bank.operatorinterface.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(long userId) {
        super(String.format("User of id=%d not found", userId));
    }
}
