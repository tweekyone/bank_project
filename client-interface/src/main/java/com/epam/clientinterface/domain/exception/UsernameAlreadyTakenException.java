package com.epam.clientinterface.domain.exception;

public class UsernameAlreadyTakenException extends RuntimeException {
    public UsernameAlreadyTakenException(String username) {
        super(String.format("User with username %s is already exists", username));
    }
}
