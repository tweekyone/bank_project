package com.epam.clientinterface.entity;

import javax.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
@Embeddable
public class TransactionAccountData {
    private String accountNumber;
    private boolean isExternal;

    public TransactionAccountData(@NonNull String accountNumber, boolean isExternal) {
        this.accountNumber = accountNumber;
        this.isExternal = isExternal;
    }
}
