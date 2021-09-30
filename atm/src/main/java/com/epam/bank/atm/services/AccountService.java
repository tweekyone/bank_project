package com.epam.bank.atm.services;

public interface AccountService {
    void withdraw(Long accountId, Double amount) throws Exception;
}
