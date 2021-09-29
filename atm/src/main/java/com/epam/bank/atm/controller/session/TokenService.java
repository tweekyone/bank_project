package com.epam.bank.atm.controller.session;

public interface TokenService {
    boolean isExpired(String token);
}
