package com.epam.bank.atm.service;

public class AccountServiceImpl implements AccountService{
    @Override
    public void withdraw(Long accountId, Double amount) throws Exception{
        // amount > account, amount < 0, amount == Nan, amount == -Inf/Inf
        //     throw new Exception("TBD");
        //
    }
}
