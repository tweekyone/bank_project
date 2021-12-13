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
import com.epam.clientinterface.service.util.DomainLogicChecker;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public void internalTransfer(long sourceAccountId, long destinationAccountId, double amount, long userId) {
        var sourceAccount = accountRepository.findAccountByIdWithUser(sourceAccountId, userId).orElseThrow(
            () -> new AccountNotFoundException(sourceAccountId)
        );

        checkIfAccountBelongsToUser(sourceAccount, userId);

        var destinationAccount = accountRepository.findAccountByIdWithUser(destinationAccountId, userId).orElseThrow(
            () -> new AccountNotFoundException(destinationAccountId)
        );

        checkIfAccountBelongsToUser(destinationAccount, userId);

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

    public void externalTransfer(long sourceAccountId, String destinationAccountNumber, double amount, long userId) {
        var sourceAccount = accountRepository.findAccountByIdWithUser(sourceAccountId, userId).orElseThrow(
            () -> new AccountNotFoundException(sourceAccountId)
        );
        checkIfAccountBelongsToUser(sourceAccount, userId);
        DomainLogicChecker.assertAccountIsNotClosed(sourceAccount);

        this.accountRepository.findByNumber(destinationAccountNumber).ifPresent(account -> {
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
        var account = accountRepository.findAccountByIdWithUser(accountId, userId).orElseThrow(
            () -> new AccountNotFoundException(accountId)
        );

        checkIfAccountBelongsToUser(account, userId);
        DomainLogicChecker.assertAccountIsNotClosed(account);
        account.close();
        accountRepository.save(account);
    }

    private void checkIfAccountBelongsToUser(Account account, long userId) {
        if (account.getUser().getId() != userId) {
            throw new AccountNotFoundException(account.getId());
        }
    }
}
