package com.epam.bank.operatorinterface.exception;

import org.springframework.security.core.AuthenticationException;

public class InitialAuthenticationFilterException extends AuthenticationException {
    public InitialAuthenticationFilterException(String msg) {
        super(msg);
    }
}
