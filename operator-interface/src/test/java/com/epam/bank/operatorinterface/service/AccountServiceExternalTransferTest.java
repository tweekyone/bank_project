package com.epam.bank.operatorinterface.service;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.bank.operatorinterface.entity.Transaction;
import com.epam.bank.operatorinterface.enumerated.TransactionOperationType;
import com.epam.bank.operatorinterface.enumerated.TransactionState;
import com.epam.bank.operatorinterface.exception.AccountIsClosedException;
import com.epam.bank.operatorinterface.exception.AccountIsNotSupposedForExternalTransferException;
import com.epam.bank.operatorinterface.exception.AccountIsNotSupposedForWithdrawException;
import com.epam.bank.operatorinterface.exception.AccountNotFoundException;
import com.epam.bank.operatorinterface.exception.NotEnoughMoneyException;
import com.epam.bank.operatorinterface.repository.AccountRepository;
import com.epam.bank.operatorinterface.repository.TransactionRepository;
import com.epam.bank.operatorinterface.repository.UserRepository;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.TestDataFactory;

@ExtendWith(MockitoExtension.class)
public class AccountServiceExternalTransferTest {

    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private AccountRepository accountRepositoryMock;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountServiceMock;

    @Test
    public void shouldCreateTransactionIfDataIsValid() {

        var sourceAccount = TestDataFactory.getAccount();
        var destinationAccount = RandomStringUtils.randomNumeric(20);
        var amount = sourceAccount.getAmount() - 100;

        when(accountRepositoryMock.findById(sourceAccount.getId())).thenReturn(Optional.of(sourceAccount));
        when(accountRepositoryMock.findAccountByNumber(destinationAccount))
            .thenReturn(Optional.empty());

        accountServiceMock.externalTransferByAccount(sourceAccount.getId(), destinationAccount,amount);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());

        Assertions.assertEquals(transactionCaptor.getValue().getSourceAccount().getAccountNumber(),
            sourceAccount.getNumber());
        Assertions.assertEquals(transactionCaptor.getValue().getDestinationAccount().getAccountNumber(),
            destinationAccount);
        Assertions.assertEquals(transactionCaptor.getValue().getAmount(), amount);
        Assertions.assertEquals(transactionCaptor.getValue().getOperationType(),
            TransactionOperationType.EXTERNAL_TRANSFER);
        Assertions.assertEquals(transactionCaptor.getValue().getState(),
            TransactionState.SUCCESS);
    }

    @Test
    void shouldThrowAccountNotFoundIfSourceAccountDoesNotExist() {
        Assertions.assertThrows(AccountNotFoundException.class,
            () -> accountServiceMock.externalTransferByAccount(RandomUtils.nextLong(),
                RandomStringUtils.randomNumeric(20),
                RandomUtils.nextDouble(1000.0, 10000.0)));
    }

    @Test
    void shouldThrowAccountIsClosedIfSourceAccountIsClosed() {
        var sourceAccount = TestDataFactory.getClosedAccount();
        var destinationAccount = RandomStringUtils.randomNumeric(20);

        when(accountRepositoryMock.findById(sourceAccount.getId())).thenReturn(Optional.of(sourceAccount));

        Assertions.assertThrows(AccountIsClosedException.class,
            () -> accountServiceMock.externalTransferByAccount(sourceAccount.getId(),
                destinationAccount,
                RandomUtils.nextDouble(1000.0, 10000.0)));
    }

    @Test
    void shouldThrowAccountIsNotSupposedForExternalTransferIfDestinationAccountExist() {
        var sourceAccount = TestDataFactory.getAccount();
        var destinationAccount = TestDataFactory.getAccount();

        when(accountRepositoryMock.findById(sourceAccount.getId())).thenReturn(Optional.of(sourceAccount));
        when(accountRepositoryMock.findAccountByNumber(destinationAccount.getNumber()))
            .thenReturn(Optional.of(destinationAccount));

        Assertions.assertThrows(AccountIsNotSupposedForExternalTransferException.class,
            () -> accountServiceMock.externalTransferByAccount(sourceAccount.getId(),
                destinationAccount.getNumber(),
                RandomUtils.nextDouble(1000.0, 10000.0)));
    }

    @Test
    void shouldThrowAccountIsNotSupposedForWithdrawIfSourceAccountIsInvest() {
        var sourceAccount = TestDataFactory.getInvestAccount();
        var destinationAccount = RandomStringUtils.randomNumeric(20);

        when(accountRepositoryMock.findById(sourceAccount.getId())).thenReturn(Optional.of(sourceAccount));

        Assertions.assertThrows(AccountIsNotSupposedForWithdrawException.class,
            () -> accountServiceMock.externalTransferByAccount(sourceAccount.getId(),
                destinationAccount,
                RandomUtils.nextDouble(1000.0, 10000.0)));
    }

    @Test
    void shouldThrowNotEnoughMoneyIfSourceAccountDoesNotHaveEnoughMoney() {

        var sourceAccount = TestDataFactory.getAccount();
        var destinationAccount = RandomStringUtils.randomNumeric(20);
        var amount = sourceAccount.getAmount() + 100;

        when(accountRepositoryMock.findById(sourceAccount.getId())).thenReturn(Optional.of(sourceAccount));

        Assertions.assertThrows(NotEnoughMoneyException.class,
            () -> accountServiceMock.externalTransferByAccount(sourceAccount.getId(),
                destinationAccount,
                amount));

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(transactionRepository).save(transactionCaptor.capture());

        Assertions.assertEquals(transactionCaptor.getValue().getSourceAccount().getAccountNumber(),
            sourceAccount.getNumber());
        Assertions.assertEquals(transactionCaptor.getValue().getDestinationAccount().getAccountNumber(),
            destinationAccount);
        Assertions.assertEquals(transactionCaptor.getValue().getAmount(), amount);
        Assertions.assertEquals(transactionCaptor.getValue().getOperationType(),
            TransactionOperationType.EXTERNAL_TRANSFER);
        Assertions.assertEquals(transactionCaptor.getValue().getState(),
            TransactionState.DECLINE);
    }
}
