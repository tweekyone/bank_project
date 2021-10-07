package com.epam.bank.atm.domain.statement;

import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

public class CardPinCodeFormatIsValid implements Statement {
    private final String pinCode;

    public CardPinCodeFormatIsValid(@NonNull String pinCode) {
        this.pinCode = pinCode;
    }

    @Override
    public boolean check() {
        return StringUtils.isNumeric(this.pinCode) && this.pinCode.length() == 4;
    }

    @Override
    public String errorMessage() {
        return "Card pin code format is invalid";
    }
}
