package com.epam.clientinterface.domain.exception;

public class IncorrectPinException extends RuntimeException {
    public IncorrectPinException(String message) {super(String.format("New pin is incorrect: %s", message));}
}
