package com.epam.bank.operatorinterface.configuration;

import com.epam.bank.operatorinterface.configuration.util.InternalRequestConstrain;
import com.epam.bank.operatorinterface.controller.dto.request.InternalTransferRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class InternalRequestValidator
    implements ConstraintValidator<InternalRequestConstrain, InternalTransferRequest> {

    @Override
    public void initialize(InternalRequestConstrain constraintAnnotation) {
    }

    @Override
    public boolean isValid(InternalTransferRequest value, ConstraintValidatorContext context) {
        var destinationAccountNumber = value.getDestinationAccountNumber();
        var destinationCardNumber = value.getDestinationCardNumber();

        if ((destinationAccountNumber == null) == (destinationCardNumber == null)) {
            context.buildConstraintViolationWithTemplate("invalid account number or card number")
                .addPropertyNode("destinationAccountNumber")
                .addConstraintViolation();

            context.buildConstraintViolationWithTemplate("invalid account number or card number")
                .addPropertyNode("destinationCardNumber")
                .addConstraintViolation();
            return false;
        }
        return true;
    }
}
