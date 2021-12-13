package com.epam.clientinterface.service;

import static com.epam.clientinterface.util.TestDataFactory.getAccount;
import static com.epam.clientinterface.util.TestDataFactory.getAccountBelongsToUser;
import static com.epam.clientinterface.util.TestDataFactory.getClosedAccountBelongsToUser;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.epam.clientinterface.domain.exception.AccountIsClosedException;
import com.epam.clientinterface.domain.exception.AccountIsNotSupposedForExternalTransferException;
import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.domain.exception.NotEnoughMoneyException;
import com.epam.clientinterface.repository.AccountRepository;
import com.epam.clientinterface.repository.TransactionRepository;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceExternalTransferTest {
    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepositoryMock;

    @Mock
    private TransactionRepository transactionRepositoryMock;

    @Test
    void shouldReturnNothingIfAccountsExistAndThereIsEnoughMoney() {
        var sourceAccountFixture = getAccountBelongsToUser();
        when(accountRepositoryMock.findAccountByIdWithUser(anyLong(), anyLong())).thenReturn(
            Optional.of(sourceAccountFixture));
        when(accountRepositoryMock.findByNumber(anyString())).thenReturn(Optional.empty());

        accountService.externalTransfer(
            sourceAccountFixture.getId(), RandomStringUtils.randomNumeric(20), RandomUtils.nextDouble(1000.0, 10000.0),
            1L
        );
    }

    @Test
    void shouldThrowAccountNotFoundIfTheSourceAccountDoesNotExist() {
        when(this.accountRepositoryMock.findAccountByIdWithUser(anyLong(), anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(
            AccountNotFoundException.class,
            () -> this.accountService.externalTransfer(
                1L, RandomStringUtils.randomNumeric(20), RandomUtils.nextDouble(1000.0, 10000.0), 1
            )
        );
    }

    @Test
    void shouldThrowAccountIsNotSupposedForExternalTransferDestinationAccountExists() {
        var sourceAccountFixture = getAccountBelongsToUser();
        var destinationAccountFixture = getAccount();
        when(accountRepositoryMock.findAccountByIdWithUser(anyLong(), anyLong())).thenReturn(Optional.of(
            sourceAccountFixture));
        when(accountRepositoryMock.findByNumber(anyString())).thenReturn(Optional.of(destinationAccountFixture));

        Assertions.assertThrows(
            AccountIsNotSupposedForExternalTransferException.class,
            () -> accountService.externalTransfer(
                sourceAccountFixture.getId(),
                destinationAccountFixture.getNumber(),
                RandomUtils.nextDouble(1000.0, 10000.0), 1
            )
        );
    }

    @Test
    void shouldThrowNotEnoughMoneyIfSourceAccountDoesNotHaveEnoughMoney() {
        var sourceAccountFixture = getAccountBelongsToUser();
        when(accountRepositoryMock.findAccountByIdWithUser(anyLong(), anyLong())).thenReturn(
            Optional.of(sourceAccountFixture));
        when(accountRepositoryMock.findByNumber(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(
            NotEnoughMoneyException.class,
            () -> accountService.externalTransfer(
                sourceAccountFixture.getId(),
                RandomStringUtils.randomNumeric(20),
                RandomUtils.nextDouble(100000.0, 1000000.0), 1
            )
        );
    }

    @Test
    void shouldThrowAccountIsClosedIfAccountIsClosed() {
        var sourceAccountFixture = getClosedAccountBelongsToUser();

        when(accountRepositoryMock.findAccountByIdWithUser(anyLong(), anyLong()))
            .thenReturn(Optional.of(sourceAccountFixture));

        Assertions.assertThrows(
            AccountIsClosedException.class,
            () -> accountService.externalTransfer(
                sourceAccountFixture.getId(),
                RandomStringUtils.randomNumeric(20),
                RandomUtils.nextDouble(1000.0, 10000.0), 1
            )
        );
    }

    @Test
    void shouldThrowAccountNotFound_IfAccountDoesNotBelongToUsers() {
        Assertions.assertThrows(AccountNotFoundException.class,
            () -> accountService.externalTransfer(getAccount().getId(), getAccount().getNumber(), 100, 1));
    }
}
