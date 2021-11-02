package com.epam.clientinterface.controller.validator;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class EmailValidatorTest {

    private static final List<String> correctEmails
        = Arrays.asList("Vanok@gmail.com", "vano.asd@mail.com", "123_ff@YA.RU");

    private static final List<String> incorrectEmails
        = Arrays.asList("3anokgmail.com", "vano@mail", "+7911000000000000");

    @ParameterizedTest
    @MethodSource("getCorrectEmails")
    void shouldValidateCorrectEmails(String email) {
        Assertions.assertTrue(EmailValidator.validateEmail(email));
    }

    @ParameterizedTest
    @MethodSource("getIncorrectEmails")
    void shouldValidateIncorrectEmails(String email) {
        Assertions.assertFalse(EmailValidator.validateEmail(email));
    }

    private static List<String> getCorrectEmails() {
        return correctEmails;
    }

    private static List<String> getIncorrectEmails() {
        return incorrectEmails;
    }
}