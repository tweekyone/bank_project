package com.epam.clientinterface.service.util;

import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
import com.epam.clientinterface.domain.exception.IncorrectPinException;
import com.epam.clientinterface.entity.Card;

public class NewPinValidator {

    public static void validatePinCode(Card card, ChangePinRequest pinRequest){
        if (!card.getPinCode().equals(pinRequest.getOldPin())) {
            throw new IncorrectPinException("Old pin is incorrect");
        } else if (pinRequest.getNewPin().equals(pinRequest.getOldPin())) {
            throw new IncorrectPinException("New pin must be different");
        } else if (!pinRequest.getNewPin().matches("[0-9]+")) {
            throw new IncorrectPinException("Pin must contains numbers only");
        } else if (pinRequest.getNewPin().length() != 4) {
            throw new IncorrectPinException("Pin must contains 4 numbers only");
        }
    }
}
