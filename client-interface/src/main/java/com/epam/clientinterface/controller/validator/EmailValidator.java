package com.epam.clientinterface.controller.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    // Correct email must contain:
    // before @ symbol -> letters A-Z (upper or lower case), numbers 0-9, symbols ._%+-
    // after @ symbol and before dot (.)  -> letters A-Z (upper or lower case), numbers 0-9, symbols ._
    // after . -> 2-6 letters A-Z (upper or lower case)
    // example: john_doe@gmail.com
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
        Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        if (matcher.find()) {
            System.out.println("Correct!");
        }
        return matcher.matches();
    }

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return (validateEmail(email));
    }
}
