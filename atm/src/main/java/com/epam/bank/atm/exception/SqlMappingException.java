package com.epam.bank.atm.exception;


public class SqlMappingException extends RuntimeException {
    public SqlMappingException() {
    }

    public SqlMappingException(String message) {
        super(message);
    }

    public SqlMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public SqlMappingException(Throwable cause) {
        super(cause);
    }

    protected SqlMappingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
