package com.epam.bank.atm.domain.statement;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

public class CardPinCodeFormatIsValid extends Statement {
    private final String pinCode;

    public CardPinCodeFormatIsValid(@NonNull String pinCode) {
        this.pinCode = pinCode;
    }

    @Override
    public boolean check() {
        return StringUtils.isNumeric(this.pinCode) && this.pinCode.length() == 4;
    }

    @Override
    protected DomainException exception() {
        return new CardPinCodeFormatIsInvalidException();
    }

    public class CardPinCodeFormatIsInvalidException extends DomainException {
        public CardPinCodeFormatIsInvalidException() {
            super("Card pin code format is invalid");
        }
    }
}
