package com.epam.bank.operatorinterface.util;

import com.epam.bank.operatorinterface.exception.ValidationException;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ValidationFacade {
    private final Validator validator;

    public Set<ConstraintViolation<Object>> validate(Object object) {
        return validator.validate(object);
    }

    public void validateOrThrow(Object object) {
        var violations = validate(object);

        if (violations.size() > 0) {
            throw new ValidationException(violations);
        }
    }
}
