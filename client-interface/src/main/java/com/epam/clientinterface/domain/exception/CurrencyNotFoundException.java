package com.epam.clientinterface.domain.exception;

public class CurrencyNotFoundException extends RuntimeException {
    public CurrencyNotFoundException(String currency) {
        super(String.format("Currency %s not found", currency));
    }

    public CurrencyNotFoundException(String currencyFrom, String currencyTo) {
        super(String.format("One or both currencies are not found: %s, %s",
            currencyFrom, currencyTo));
    }
}
