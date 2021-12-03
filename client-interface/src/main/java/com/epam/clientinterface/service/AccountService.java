package com.epam.clientinterface.service;

import com.epam.clientinterface.domain.exception.AccountIsNotSupposedForExternalTransferException;
import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.domain.exception.NotEnoughMoneyException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.Transaction;
import com.epam.clientinterface.entity.TransactionAccountData;
import com.epam.clientinterface.entity.TransactionOperationType;
import com.epam.clientinterface.entity.TransactionState;
import com.epam.clientinterface.repository.AccountRepository;
import com.epam.clientinterface.repository.TransactionRepository;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public void internalTransfer(long sourceAccountId, long destinationAccountId, double amount) {
        var sourceAccount = accountRepository.findById(sourceAccountId)
            .orElseThrow(() -> new AccountNotFoundException(sourceAccountId));
        var destinationAccount = accountRepository.findById(destinationAccountId)
            .orElseThrow(() -> new AccountNotFoundException(destinationAccountId));

        DomainLogicChecker.assertAccountIsNotClosed(sourceAccount);
        DomainLogicChecker.assertAccountIsNotClosed(destinationAccount);

        if (sourceAccount.getAmount() < amount) {
            transactionRepository.save(new Transaction(
                new TransactionAccountData(sourceAccount.getNumber(), false),
                new TransactionAccountData(destinationAccount.getNumber(), false),
                amount,
                TransactionOperationType.INTERNAL_TRANSFER,
                TransactionState.DECLINE
            ));

            throw new NotEnoughMoneyException(sourceAccountId, amount);
        }

        sourceAccount.setAmount(sourceAccount.getAmount() - amount);
        destinationAccount.setAmount(destinationAccount.getAmount() + amount);

        accountRepository.saveAll(new ArrayIterator<>(new Account[] {sourceAccount, destinationAccount}));
        transactionRepository.save(new Transaction(
            new TransactionAccountData(sourceAccount.getNumber(), false),
            new TransactionAccountData(destinationAccount.getNumber(), false),
            amount,
            TransactionOperationType.INTERNAL_TRANSFER,
            TransactionState.SUCCESS
        ));
    }

    public void externalTransfer(long sourceAccountId, String destinationAccountNumber, double amount) {
        var sourceAccount = accountRepository.findById(sourceAccountId)
            .orElseThrow(() -> new AccountNotFoundException(sourceAccountId));

        DomainLogicChecker.assertAccountIsNotClosed(sourceAccount);

        accountRepository.findByNumber(destinationAccountNumber).ifPresent(account -> {
            throw new AccountIsNotSupposedForExternalTransferException(account.getId());
        });

        if (sourceAccount.getAmount() < amount) {
            transactionRepository.save(new Transaction(
                new TransactionAccountData(sourceAccount.getNumber(), false),
                new TransactionAccountData(destinationAccountNumber, true),
                amount,
                TransactionOperationType.EXTERNAL_TRANSFER,
                TransactionState.DECLINE
            ));

            throw new NotEnoughMoneyException(sourceAccountId, amount);
        }

        transactionRepository.save(new Transaction(
            new TransactionAccountData(sourceAccount.getNumber(), false),
            new TransactionAccountData(destinationAccountNumber, true),
            amount,
            TransactionOperationType.EXTERNAL_TRANSFER,
            TransactionState.SUCCESS
        ));
    }

    public void closeAccount(long accountId, long userId) {
        var account = accountRepository.findById(accountId)
            .orElseThrow(() -> new AccountNotFoundException(accountId));

        if (account.getUser().getId() != userId) {
            throw new AccountNotFoundException(accountId);
        }

        DomainLogicChecker.assertAccountIsNotClosed(account);

        account.close();

        accountRepository.save(account);
    }
}
