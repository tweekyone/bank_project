package com.epam.clientinterface.domain.listener;

import com.epam.clientinterface.domain.event.TransferWasSucceed;
import com.epam.clientinterface.entity.Transaction;
import com.epam.clientinterface.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TransferWasSucceedListener {
    private final TransactionRepository transactionRepository;

    @EventListener
    public void handle(TransferWasSucceed event) {
        this.transactionRepository.save(new Transaction(
            event.getSourceAccount(),
            event.getDestinationAccount(),
            event.getAmount(),
            event.getOperationType(),
            Transaction.State.SUCCESS
        ));
    }
}
