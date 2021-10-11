package com.epam.bank.atm.entity;

import com.epam.bank.atm.domain.statement.Assertion;
import com.epam.bank.atm.domain.statement.CardAccountExists;
import com.epam.bank.atm.domain.statement.CardNumberFormatIsValid;
import com.epam.bank.atm.domain.statement.CardPinCodeFormatIsValid;
import com.epam.bank.atm.domain.statement.CardWithSuchNumberDoesNotExist;
import lombok.Getter;
import lombok.NonNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Card {
    private Long id;
    private final String number;
    private final long accountId;
    private final String pinCode;
    private final Plan plan;
    // ToDo: is the field exactly supposed to be named "explication_date"
    private final LocalDateTime explicationDate;

    public enum Plan {
        TESTPLAN
    }

    public Card(
        @NonNull String number,
        long accountId,
        @NonNull String pinCode,
        @NonNull Plan plan,
        @NonNull LocalDateTime explicationDate
    ) {
        Assertion.assertA(new CardNumberFormatIsValid(number));
        Assertion.assertA(new CardWithSuchNumberDoesNotExist(number));
        Assertion.assertA(new CardAccountExists(accountId));
        Assertion.assertA(new CardPinCodeFormatIsValid(pinCode));

        this.number = number;
        this.pinCode = pinCode;
        this.plan = plan;
        this.explicationDate = explicationDate;
        this.accountId = accountId;
    }

    // Hydration constructor
    public Card(
        long id,
        @NonNull String number,
        long accountId,
        @NonNull String pinCode,
        @NonNull Plan plan,
        @NonNull LocalDateTime explicationDate
    ) {
        this.id = id;
        this.number = number;
        this.pinCode = pinCode;
        this.plan = plan;
        this.explicationDate = explicationDate;
        this.accountId = accountId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Card card = (Card) o;
        return Objects.equals(id, card.id)
            && Objects.equals(number, card.number)
            && accountId == card.accountId
            && Objects.equals(pinCode, card.pinCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, number, accountId, pinCode);
    }
}
