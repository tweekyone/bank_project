package com.epam.bank.atm.domain.statement;

import com.epam.bank.atm.domain.DomainRegistry;

public class CardAccountExists extends Statement {
    private final long accountId;

    public CardAccountExists(long accountId) {
        this.accountId = accountId;
    }

    @Override
    public boolean check() {
        return DomainRegistry.getAccountRepository().getById(this.accountId) != null;
    }

    @Override
    protected DomainException exception() {
        return new CardAccountDoesNotExistException(this.accountId);
    }

    public class CardAccountDoesNotExistException extends DomainException {
        public CardAccountDoesNotExistException(long accountId) {
            super(String.format("Card account %d does not exist", accountId));
        }
    }
}
