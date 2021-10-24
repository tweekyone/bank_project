package com.epam.clientinterface.service;

import com.epam.clientinterface.domain.event.TransferWasDeclined;
import com.epam.clientinterface.domain.event.TransferWasSucceed;
import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.domain.exception.NotEnoughMoneyException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Transaction;
import com.epam.clientinterface.repository.AccountRepository;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void transfer(long sourceAccountId, long destinationAccountId, double amount) {
        var sourceAccount = this.accountRepository.findById(sourceAccountId).orElseThrow(() -> {
            throw new AccountNotFoundException(sourceAccountId);
        });
        var destinationAccount = this.accountRepository.findById(destinationAccountId).orElseThrow(() -> {
            throw new AccountNotFoundException(destinationAccountId);
        });

        if (sourceAccount.getAmount() < amount) {
            this.eventPublisher.publishEvent(new TransferWasDeclined(
                new Transaction.AccountData(sourceAccount.getNumber(), false),
                new Transaction.AccountData(destinationAccount.getNumber(), false),
                amount,
                Transaction.OperationType.INNER_TRANSFER
            ));

            throw new NotEnoughMoneyException(sourceAccountId, amount);
        }

        sourceAccount.setAmount(sourceAccount.getAmount() - amount);
        destinationAccount.setAmount(destinationAccount.getAmount() + amount);

        this.accountRepository.saveAll(new ArrayIterator<>(new Account[] {sourceAccount, destinationAccount}));

        this.eventPublisher.publishEvent(new TransferWasSucceed(
            new Transaction.AccountData(sourceAccount.getNumber(), false),
            new Transaction.AccountData(destinationAccount.getNumber(), false),
            amount,
            Transaction.OperationType.INNER_TRANSFER
        ));
    }
}
