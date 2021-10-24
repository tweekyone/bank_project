package com.epam.clientinterface.domain.listener;

import com.epam.clientinterface.domain.event.TransferWasDeclined;
import com.epam.clientinterface.entity.Transaction;
import com.epam.clientinterface.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransferWasDeclinedListener {
    private final TransactionRepository transactionRepository;

    @EventListener
    public void handle(TransferWasDeclined event) {
        this.transactionRepository.save(new Transaction(
            event.getSourceAccount(),
            event.getDestinationAccount(),
            event.getAmount(),
            event.getOperationType(),
            Transaction.State.DECLINE
        ));
    }
}
