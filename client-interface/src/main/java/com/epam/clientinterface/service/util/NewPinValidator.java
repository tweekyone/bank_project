package com.epam.clientinterface.service.util;

import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
import com.epam.clientinterface.domain.exception.IncorrectPinException;
import com.epam.clientinterface.entity.Card;

public class NewPinValidator {

    public static void validatePinCode(Card card, ChangePinRequest pinRequest) {
        if (pinRequest.getNewPin().equals(card.getPinCode())) {
            throw new IncorrectPinException("New pin must be different");
        } else if (!pinRequest.getNewPin().matches("[0-9]{4}")) {
            throw new IncorrectPinException("Pin must contains four numbers only");
        }
    }
}
