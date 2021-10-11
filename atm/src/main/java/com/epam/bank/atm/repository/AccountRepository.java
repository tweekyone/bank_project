package com.epam.bank.atm.repository;

import com.epam.bank.atm.entity.Account;
import java.math.BigInteger;

public interface AccountRepository {
    Account getById(long id);

    double putMoney(long id, double amount);

    double withdrawMoney(long id, double amount);

    double getCurrentAmount(long id);

    BigInteger getAccountNumberById(long id);
}
