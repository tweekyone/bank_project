package com.epam.clientinterface.entity;

import java.util.Arrays;
import java.util.List;

public enum Currency {
    EUR,
    USD,
    RUB,
    JPY;

    public static List<Currency> getCurrencies() {
        return Arrays.asList(EUR, USD, RUB, JPY);
    }
}
