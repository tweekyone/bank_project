package com.epam.clientinterface.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {

    public static final Pattern VALID_CURRENCY_REGEX =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateCurrency(String currency) {
        Matcher matcher = VALID_CURRENCY_REGEX.matcher(currency);
        if (matcher.find()) {
            System.out.println("Correct currency!");
        }
        return matcher.matches();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return validateCurrency(value);
    }

    @Override
    public void initialize(ValidCurrency constraintAnnotation) {

    }
}
