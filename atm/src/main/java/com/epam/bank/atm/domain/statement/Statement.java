package com.epam.bank.atm.domain.statement;

public interface Statement {
    boolean check();

    String errorMessage();
}
