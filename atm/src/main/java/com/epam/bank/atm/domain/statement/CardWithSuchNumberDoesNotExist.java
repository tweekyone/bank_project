package com.epam.bank.atm.domain.statement;

import com.epam.bank.atm.domain.DomainRegistry;
import lombok.NonNull;

public class CardWithSuchNumberDoesNotExist extends Statement {
    private final String number;

    public CardWithSuchNumberDoesNotExist(@NonNull String number) {
        this.number = number;
    }

    @Override
    public boolean check() {
        return DomainRegistry.getCardRepository().getByNumber(this.number).isEmpty();
    }

    @Override
    protected DomainException exception() {
        return new CardWithSuchNumberAlreadyExistsException(this.number);
    }

    public class CardWithSuchNumberAlreadyExistsException extends DomainException {
        public CardWithSuchNumberAlreadyExistsException(String number) {
            super(String.format("Card with number %s already exists", number));
        }
    }
}
