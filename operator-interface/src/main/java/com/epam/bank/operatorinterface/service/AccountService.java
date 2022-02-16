package com.epam.bank.operatorinterface.service;

import com.epam.bank.operatorinterface.entity.Account;
import com.epam.bank.operatorinterface.entity.Transaction;
import com.epam.bank.operatorinterface.entity.TransactionAccountData;
import com.epam.bank.operatorinterface.enumerated.AccountPlan;
import com.epam.bank.operatorinterface.enumerated.AccountType;
import com.epam.bank.operatorinterface.enumerated.TransactionOperationType;
import com.epam.bank.operatorinterface.enumerated.TransactionState;
import com.epam.bank.operatorinterface.exception.AccountCanNotBeClosedException;
import com.epam.bank.operatorinterface.exception.AccountIsClosedException;
import com.epam.bank.operatorinterface.exception.AccountIsNotSupposedForExternalTransferException;
import com.epam.bank.operatorinterface.exception.AccountIsNotSupposedForWithdrawException;
import com.epam.bank.operatorinterface.exception.AccountNotFoundException;
import com.epam.bank.operatorinterface.exception.AccountNumberGenerationTriesLimitException;
import com.epam.bank.operatorinterface.exception.CardNotFoundException;
import com.epam.bank.operatorinterface.exception.NotEnoughMoneyException;
import com.epam.bank.operatorinterface.exception.TransferException;
import com.epam.bank.operatorinterface.exception.UserNotFoundException;
import com.epam.bank.operatorinterface.repository.AccountRepository;
import com.epam.bank.operatorinterface.repository.TransactionRepository;
import com.epam.bank.operatorinterface.repository.UserRepository;
import com.fasterxml.jackson.databind.util.ArrayIterator;
import javax.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public Account create(long userId, AccountPlan plan) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        var isDefault = user.getActiveAccounts().size() == 0;
        var account = new Account(user, generateNumber(), isDefault, plan);

        return accountRepository.save(account);
    }

    private String generateNumber() {
        String number;
        var triesLimit = 100;
        do {
            number = RandomStringUtils.randomNumeric(20);
            triesLimit--;
        } while (accountRepository.existsByNumber(number) && triesLimit > 0);

        if (accountRepository.existsByNumber(number)) {
            throw new AccountNumberGenerationTriesLimitException();
        }

        return number;
    }

    @Transactional
    public void makeDefault(long accountId) {
        var account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));

        assertAccountIsNotClosed(account);

        account.getUser().getAccounts().forEach(Account::makeNotDefault);
        account.makeDefault();

        userRepository.save(account.getUser());
    }

    @Transactional
    public void close(long accountId) {
        var account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));

        assertAccountIsNotClosed(account);
        assertAccountCanBeClosed(account);

        account.close();

        accountRepository.save(account);
    }

    public static void assertAccountIsNotClosed(Account account) {
        if (!account.isActive()) {
            throw new AccountIsClosedException(account.getId());
        }
    }

    private static void assertAccountCanBeClosed(Account account) {
        if (account.isDefault() && account.isActive() && account.getUser().getActiveAccounts().size() > 1) {
            throw new AccountCanNotBeClosedException(account.getId());
        }
    }

    private static void assertAccountIsSuitableForWithdraw(Account account) {
        if (account.getType() == AccountType.INVEST) {
            throw new AccountIsNotSupposedForWithdrawException(account.getId());
        }
    }

    private static void assertThatAccountsAreDifference(Account sourceAccount, Account destinationAccount) {
        if (sourceAccount.equals(destinationAccount)) {
            throw new TransferException();
        }
    }

    private static void assertAccountHasEnoughMoneyForWithdraw(Account account, double amount) {
        if (account.getAmount() < amount) {
            throw new NotEnoughMoneyException(account.getId(), amount);
        }
    }

    public void internalTransferByCard(long sourceAccountId, String destinationCardNumber, double amount) {

        var destinationAccount = accountRepository.findAccountByCardNumber(destinationCardNumber)
            .orElseThrow(() -> new CardNotFoundException(destinationCardNumber));

        internalTransferMoney(sourceAccountId, destinationAccount, amount);
    }

    public void internalTransferByAccount(long sourceAccountId,String destinationAccountNumber,
                                          double amount) {

        var destinationAccount = accountRepository.findAccountByNumber(destinationAccountNumber)
            .orElseThrow(() -> new AccountNotFoundException(destinationAccountNumber));

        internalTransferMoney(sourceAccountId, destinationAccount, amount);
    }

    public void externalTransferByAccount(long sourceAccountId, String destinationAccountNumber, double amount) {

        var sourceAccount = accountRepository.findById(sourceAccountId)
            .orElseThrow(() -> new AccountNotFoundException(sourceAccountId));

        assertAccountIsNotClosed(sourceAccount);
        assertAccountIsSuitableForWithdraw(sourceAccount);

        accountRepository.findAccountByNumber(destinationAccountNumber).ifPresent(account -> {
            throw new AccountIsNotSupposedForExternalTransferException(account.getNumber());
        });

        try {
            assertAccountHasEnoughMoneyForWithdraw(sourceAccount, amount);
        } catch (NotEnoughMoneyException ex) {
            createTransaction(sourceAccount.getNumber(), destinationAccountNumber, amount,
                TransactionOperationType.EXTERNAL_TRANSFER, TransactionState.DECLINE);

            throw ex;
        }

        sourceAccount.setAmount(sourceAccount.getAmount() - amount);
        accountRepository.save(sourceAccount);

        createTransaction(sourceAccount.getNumber(), destinationAccountNumber, amount,
            TransactionOperationType.EXTERNAL_TRANSFER, TransactionState.SUCCESS);
    }

    protected void internalTransferMoney(long sourceAccountId,Account destinationAccount, double amount) {
        var sourceAccount = accountRepository.findById(sourceAccountId)
            .orElseThrow(() -> new AccountNotFoundException(sourceAccountId));
       
        assertThatAccountsAreDifference(sourceAccount, destinationAccount);
        assertAccountIsNotClosed(sourceAccount);
        assertAccountIsSuitableForWithdraw(sourceAccount);
        assertAccountIsNotClosed(destinationAccount);

        try {
            assertAccountHasEnoughMoneyForWithdraw(sourceAccount, amount);
        } catch (NotEnoughMoneyException ex) {
            createTransaction(sourceAccount.getNumber(), destinationAccount.getNumber(), amount,
                TransactionOperationType.INTERNAL_TRANSFER, TransactionState.DECLINE);

            throw ex;
        }

        sourceAccount.setAmount(sourceAccount.getAmount() - amount);
        destinationAccount.setAmount(destinationAccount.getAmount() + amount);
        accountRepository.saveAll(new ArrayIterator<>(new Account[] {sourceAccount, destinationAccount}));

        createTransaction(sourceAccount.getNumber(), destinationAccount.getNumber(), amount,
            TransactionOperationType.INTERNAL_TRANSFER, TransactionState.SUCCESS);
    }

    protected void createTransaction(String sourceAccount, String destinationAccount, double amount,
                                     TransactionOperationType type, TransactionState state) {
        transactionRepository.save(new Transaction(
            new TransactionAccountData(sourceAccount, false),
            new TransactionAccountData(destinationAccount, type == TransactionOperationType.EXTERNAL_TRANSFER),
            amount, type, state));
    }
}
