package com.epam.clientinterface.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String email) {
        super(String.format("Account with email %s already exists", email));
    }

}
