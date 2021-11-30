package com.epam.clientinterface.service.util;

import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
import com.epam.clientinterface.domain.exception.IncorrectPinException;
import com.epam.clientinterface.entity.Card;

public class PinValidator {

    public static void validatePinCode(Card card, ChangePinRequest pinRequest) {
        String message;
        if (!card.getPinCode().equals(pinRequest.getOldPin())) {
            message = "Old pin is incorrect";
        } else if (pinRequest.getNewPin().equals(pinRequest.getOldPin())) {
            message = "New pin must be different";
        } else if (!pinRequest.getNewPin().matches("[0-9]{4}")) {
            message = "Pin must contains numbers only";
        } else {
            return;
        }
        throw new IncorrectPinException(message);
    }
}
