package com.epam.bank.atm.entity;

import com.epam.bank.atm.domain.statement.Assertion;
import com.epam.bank.atm.domain.statement.CardAccountExists;
import com.epam.bank.atm.domain.statement.CardNumberFormatIsValid;
import com.epam.bank.atm.domain.statement.CardPinCodeFormatIsValid;
import com.epam.bank.atm.domain.statement.CardWithSuchNumberDoesNotExist;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Card {
    private Long id;
    private String number;
    private long accountId;
    private String pinCode;
    private Plan plan;
    private ZonedDateTime expirationDate;
    private boolean isBlocked;
    private int pinCounter;

    public enum Plan {
        BASE
    }

    public Card(
        @NonNull String number,
        long accountId,
        @NonNull String pinCode,
        @NonNull Plan plan,
        @NonNull ZonedDateTime expirationDate
    ) {
        Assertion.assertA(new CardNumberFormatIsValid(number));
        Assertion.assertA(new CardWithSuchNumberDoesNotExist(number));
        Assertion.assertA(new CardAccountExists(accountId));
        Assertion.assertA(new CardPinCodeFormatIsValid(pinCode));

        this.number = number;
        this.pinCode = pinCode;
        this.plan = plan;
        this.expirationDate = expirationDate;
        this.accountId = accountId;
        this.isBlocked = false;
        this.pinCounter = 0;
    }
}
