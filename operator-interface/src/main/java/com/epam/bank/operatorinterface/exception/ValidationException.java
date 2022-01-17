package com.epam.bank.operatorinterface.exception;

import java.util.Set;
import javax.validation.ConstraintViolation;
import lombok.Getter;

public class ValidationException extends RuntimeException {
    @Getter
    private Set<ConstraintViolation<Object>> violations;

    public ValidationException(Set<ConstraintViolation<Object>> violations) {
        super("Validation exception");
        this.violations = violations;
    }
}
