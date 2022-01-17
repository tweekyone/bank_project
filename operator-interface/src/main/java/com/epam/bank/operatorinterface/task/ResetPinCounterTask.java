package com.epam.bank.operatorinterface.task;

import com.epam.bank.operatorinterface.repository.CardRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ResetPinCounterTask {
    private final CardRepository cardRepository;

    @Scheduled(cron = "${app.task.reset-pin-counter.cron}")
    public void execute() {
        cardRepository.resetPinCounter();
    }
}
