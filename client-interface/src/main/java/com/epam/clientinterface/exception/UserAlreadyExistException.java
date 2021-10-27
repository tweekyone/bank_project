package com.epam.clientinterface.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String email) {
        super(String.format("User with email %s already exists", email));
        System.out.printf("User with email %s already exists", email);
    }

}
