package com.epam.bank.atm.domain.statement;

import com.epam.bank.atm.entity.TransactionAccountData;

public class AtLeastOneAccountTakePartInTransaction implements Statement {
    private final TransactionAccountData sourceAccount;
    private final TransactionAccountData destinationAccount;

    public AtLeastOneAccountTakePartInTransaction(
        TransactionAccountData sourceAccount, TransactionAccountData destinationAccount
    ) {
        this.sourceAccount = sourceAccount;
        this.destinationAccount = destinationAccount;
    }

    @Override
    public boolean check() {
        return this.sourceAccount != null || this.destinationAccount != null;
    }

    @Override
    public String errorMessage() {
        return "No one account takes part in transaction";
    }
}
