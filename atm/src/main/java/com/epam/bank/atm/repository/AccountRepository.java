package com.epam.bank.atm.repository;

import com.epam.bank.atm.entity.Account;

public interface AccountRepository {
    Account getById(long id);
}

