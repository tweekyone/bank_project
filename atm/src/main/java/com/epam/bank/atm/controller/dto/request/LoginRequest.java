package com.epam.bank.atm.controller.dto.request;

public class LoginRequest {
    private String cardNumber;
    private String pin;

    public String getCardNumber() {
        return this.cardNumber;
    }

    public String getPin() {
        return this.pin;
    }
}
