package com.epam.clientinterface.domain.exception;

public class ChangePinException extends RuntimeException {
    public ChangePinException() {
        super("You have already changed your pin for 3 times! Try again tomorrow!");
    }
}
