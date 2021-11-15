package com.epam.clientinterface.entity;

import com.epam.clientinterface.validator.ValidCurrency;
import java.util.Arrays;
import java.util.List;

@ValidCurrency
public enum Currency {
    EUR,
    USD,
    RUB,
    JPY;

    public static List<Currency> getCurrencies() {
        return Arrays.asList(EUR, USD, RUB, JPY);
    }
}
