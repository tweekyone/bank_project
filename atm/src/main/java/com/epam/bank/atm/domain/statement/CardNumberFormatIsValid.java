package com.epam.bank.atm.domain.statement;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

public class CardNumberFormatIsValid extends Statement {
    private final String number;

    public CardNumberFormatIsValid(@NonNull String number) {
        this.number = number;
    }

    @Override
    public boolean check() {
        return StringUtils.isNumeric(this.number) && (this.number.length() == 16 || this.number.length() == 18);
    }

    @Override
    protected DomainException exception() {
        return new CardNumberFormatIsInvalidException(this.number);
    }

    public class CardNumberFormatIsInvalidException extends DomainException {
        public CardNumberFormatIsInvalidException(String number) {
            super(String.format("Card number %s is invalid", number));
        }
    }
}
