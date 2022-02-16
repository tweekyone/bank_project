package com.epam.bank.operatorinterface.util;

import java.time.format.DateTimeFormatter;

public class DateFormatter {

    public static final DateTimeFormatter YEAR_MONTH =
        DateTimeFormatter.ofPattern("yyyy-MM");

    public static final DateTimeFormatter DATE_TIME =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
}
