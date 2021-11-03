package com.epam.clientinterface.service.util;

import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
import com.epam.clientinterface.domain.exception.IncorrectPinException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Card;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NewPinValidatorTest {
    private ChangePinRequest testChangePinRequest;
    private Card testCard;

    @BeforeEach
    public void beforeEach() {
        testChangePinRequest = new ChangePinRequest(1l, "1111", "2222");
        testCard = new Card(1l, "", new Account(), "1111", Card.Plan.TESTPLAN,
            LocalDateTime.now(), null);
    }

    @Test
    public void oldPinIsIncorrect() {
        testChangePinRequest.setOldPin("2222");
        IncorrectPinException thrownException = Assertions.assertThrows(IncorrectPinException.class,
            () -> NewPinValidator.validatePinCode(testCard, testChangePinRequest));

        Assertions.assertEquals("Pin is incorrect: Old pin is incorrect", thrownException.getMessage());
    }

    @Test
    public void differentPins() {
        testChangePinRequest.setNewPin("1111");
        IncorrectPinException thrownException = Assertions.assertThrows(IncorrectPinException.class,
            () -> NewPinValidator.validatePinCode(testCard, testChangePinRequest));

        Assertions.assertEquals("Pin is incorrect: New pin must be different", thrownException.getMessage());
    }

    @Test
    public void pinWithNumbersOnly() {
        testChangePinRequest.setNewPin("a111");
        IncorrectPinException thrownException = Assertions.assertThrows(IncorrectPinException.class,
            () -> NewPinValidator.validatePinCode(testCard, testChangePinRequest));

        Assertions.assertEquals("Pin is incorrect: Pin must contains numbers only", thrownException.getMessage());
    }

    @Test
    public void pinWith4Numbers() {
        testChangePinRequest.setNewPin("111");
        IncorrectPinException thrownException = Assertions.assertThrows(IncorrectPinException.class,
            () -> NewPinValidator.validatePinCode(testCard, testChangePinRequest));

        Assertions.assertEquals("Pin is incorrect: Pin must contains 4 numbers only", thrownException.getMessage());
    }
}
