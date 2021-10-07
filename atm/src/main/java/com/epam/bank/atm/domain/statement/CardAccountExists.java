package com.epam.bank.atm.domain.statement;

import com.epam.bank.atm.domain.DomainRegistry;

public class CardAccountExists implements Statement {
    private final long accountId;

    public CardAccountExists(long accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean check() {
        return DomainRegistry.getAccountRepository().getById(this.accountId) != null;
    }

    @Override
    public String errorMessage() {
        return String.format("Card account %d does not exist", this.accountId);
    }
}
