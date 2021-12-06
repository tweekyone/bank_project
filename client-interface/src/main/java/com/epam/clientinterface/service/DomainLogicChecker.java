package com.epam.clientinterface.service;

import com.epam.clientinterface.domain.exception.AccountIsClosedException;
import com.epam.clientinterface.entity.Account;

public class DomainLogicChecker {
    public static void assertAccountIsNotClosed(Account account) {
        if (account.isClosed()) {
            throw new AccountIsClosedException(account.getId());
        }
    }
}
