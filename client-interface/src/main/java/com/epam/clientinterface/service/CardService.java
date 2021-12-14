package com.epam.clientinterface.service;

import com.epam.clientinterface.controller.dto.request.ChangePinRequest;
import com.epam.clientinterface.entity.Card;
import com.epam.clientinterface.entity.CardPlan;
import java.util.Random;
import javax.validation.constraints.Positive;
import lombok.NonNull;
import org.springframework.scheduling.annotation.Scheduled;

public interface CardService {
    Card changePinCode(ChangePinRequest pinRequest);

    void dropPinCounter();

    @NonNull Card releaseCard(@NonNull Long accountId, @NonNull CardPlan plan, long userId);

    @NonNull Card blockCard(@Positive Long cardId, long userId);

    default String generateCardNumber() {
        return randomGenerateStringOfInt(16);
    }

    default String generatePinCode() {
        return randomGenerateStringOfInt(4);
    }

    default String randomGenerateStringOfInt(int length) {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10);
            builder.append(digit);
        }
        return builder.toString();

    }
}
