package com.epam.bank.clientinterface.domain.exception;

public class NotEnoughMoneyException extends RuntimeException {
    public NotEnoughMoneyException(long accountId, double amount) {
        super(String.format(
            "Account of id=%d does not have enough money to make transfer in amount %f",
            accountId,
            amount
        ));
    }
}
