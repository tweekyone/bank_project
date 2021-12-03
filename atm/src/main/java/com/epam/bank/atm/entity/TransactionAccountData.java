package com.epam.bank.atm.entity;

import lombok.NonNull;
import lombok.Value;

@Value
public class TransactionAccountData {
    String accountNumber;
    boolean isExternal;

    public TransactionAccountData(@NonNull String accountNumber, boolean isExternal) {
        this.accountNumber = accountNumber;
        this.isExternal = isExternal;
    }

    public static TransactionAccountData internal(@NonNull String accountNumber) {
        return new TransactionAccountData(accountNumber, false);
    }

    public static TransactionAccountData external(@NonNull String accountNumber) {
        return new TransactionAccountData(accountNumber, true);
    }
}
