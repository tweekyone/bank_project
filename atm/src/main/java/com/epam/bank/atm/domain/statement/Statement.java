package com.epam.bank.atm.domain.statement;

public abstract class Statement {
    abstract public boolean check();

    abstract protected DomainException exception();

    public void checkOrThrow() {
        if (!this.check()) {
            throw this.exception();
        }
    }
}
