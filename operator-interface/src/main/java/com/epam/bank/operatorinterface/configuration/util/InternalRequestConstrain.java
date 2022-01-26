package com.epam.bank.operatorinterface.configuration.util;

import com.epam.bank.operatorinterface.configuration.InternalRequestValidator;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Constraint(validatedBy = InternalRequestValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InternalRequestConstrain {
    String message() default "Invalid internalRequest body";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
