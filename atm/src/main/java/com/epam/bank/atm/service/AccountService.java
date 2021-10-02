package com.epam.bank.atm.service;

public interface AccountService {
    void withdraw(Long accountId, Double amount) throws Exception;
}
