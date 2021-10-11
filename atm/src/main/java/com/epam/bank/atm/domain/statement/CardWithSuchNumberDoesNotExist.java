package com.epam.bank.atm.domain.statement;

import com.epam.bank.atm.domain.DomainRegistry;
import lombok.NonNull;

public class CardWithSuchNumberDoesNotExist implements Statement {
    private final String number;

    public CardWithSuchNumberDoesNotExist(@NonNull String number) {
        this.number = number;
    }

    @Override
    public boolean check() {
        return DomainRegistry.getCardRepository().getByNumber(this.number).isEmpty();
    }

    @Override
    public String errorMessage() {
        return String.format("Card with number %s already exists", this.number);
    }
}
