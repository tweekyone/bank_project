package com.epam.bank.atm.domain.statement;

public class Assertion {
    public static void assertA(Statement statement) {
        statement.checkOrThrow();
    }
}
