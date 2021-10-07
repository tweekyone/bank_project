package com.epam.bank.atm.domain.statement;

public class AtLeastOneAccountTakePartInTransaction implements Statement {
    private final Long sourceAccountId;
    private final Long destinationAccountId;

    public AtLeastOneAccountTakePartInTransaction(Long sourceAccountId, Long destinationAccountId) {
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
    }

    @Override
    public boolean check() {
        return this.sourceAccountId != null || this.destinationAccountId != null;
    }

    @Override
    public String errorMessage() {
        return "No one account takes part in transaction";
    }
}
