package com.epam.bank.atm.controller;

import com.epam.bank.atm.domain.model.AuthDescriptor;

public interface TokenSessionService {
    void initFromToken(String token);
    String start(AuthDescriptor authDescriptor);
    AuthDescriptor curSession();
}
