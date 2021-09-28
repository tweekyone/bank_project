package com.epam.bank.atm.service;

import com.epam.bank.atm.domain.model.AuthDescriptor;

public interface AuthService {
    AuthDescriptor login(String login, String password);
}
