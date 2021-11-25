package com.epam.clientinterface.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.epam.clientinterface.TestDataFactory;
import com.epam.clientinterface.domain.exception.AccountIsClosedException;
import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.domain.exception.NotEnoughMoneyException;
import com.epam.clientinterface.repository.AccountRepository;
import com.epam.clientinterface.repository.TransactionRepository;
import java.util.Optional;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AccountServiceInternalTransferTest {
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepositoryMock;

    @Mock
    private TransactionRepository transactionRepositoryMock;

    @BeforeEach
    public void beforeEach() {
        this.accountService = new AccountService(this.accountRepositoryMock, this.transactionRepositoryMock);
    }

    @Test
    public void shouldReturnNothingIfAccountsExistAndThereIsEnoughMoney() {
        var sourceAccountFixture = TestDataFactory.getAccount();
        var destinationAccountFixture = TestDataFactory.getAccount();

        when(this.accountRepositoryMock.findById(anyLong()))
            .thenReturn(Optional.of(sourceAccountFixture))
            .thenReturn(Optional.of(destinationAccountFixture));

        this.accountService.internalTransfer(
            sourceAccountFixture.getId(),
            destinationAccountFixture.getId(),
            RandomUtils.nextDouble(1000.0, 10000.0)
        );
    }

    @Test
    public void shouldThrowAccountNotFoundIfTheSourceAccountDoesNotExist() {
        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(
            AccountNotFoundException.class,
            () -> this.accountService.internalTransfer(
                RandomUtils.nextLong(),
                RandomUtils.nextLong(),
                RandomUtils.nextDouble(1000.0, 10000.0)
            )
        );
    }

    @Test
    public void shouldThrowAccountNotFoundIfTheDestinationAccountDoesNotExist() {
        var sourceAccountFixture = TestDataFactory.getAccount();

        when(this.accountRepositoryMock.findById(anyLong()))
            .thenReturn(Optional.of(sourceAccountFixture))
            .thenReturn(Optional.empty());

        Assertions.assertThrows(
            AccountNotFoundException.class,
            () -> this.accountService.internalTransfer(
                sourceAccountFixture.getId(),
                RandomUtils.nextLong(),
                RandomUtils.nextDouble(1000.0, 10000.0)
            )
        );
    }

    @Test
    public void shouldThrowNotEnoughMoneyIfSourceAccountDoesNotHaveEnoughMoney() {
        var sourceAccountFixture = TestDataFactory.getAccount();
        var destinationAccountFixture = TestDataFactory.getAccount();

        when(this.accountRepositoryMock.findById(anyLong()))
            .thenReturn(Optional.of(sourceAccountFixture))
            .thenReturn(Optional.of(destinationAccountFixture));

        Assertions.assertThrows(
            NotEnoughMoneyException.class,
            () -> this.accountService.internalTransfer(
                sourceAccountFixture.getId(),
                destinationAccountFixture.getId(),
                RandomUtils.nextDouble(100000.0, 1000000.0)
            )
        );
    }

    @Test
    public void shouldThrowAccountIsClosedIfSourceAccountIsClosed() {
        var sourceAccountFixture = TestDataFactory.getClosedAccount();
        var destinationAccountFixture = TestDataFactory.getAccount();

        when(this.accountRepositoryMock.findById(anyLong()))
            .thenReturn(Optional.of(sourceAccountFixture))
            .thenReturn(Optional.of(destinationAccountFixture));

        Assertions.assertThrows(
            AccountIsClosedException.class,
            () -> this.accountService.internalTransfer(
                sourceAccountFixture.getId(),
                destinationAccountFixture.getId(),
                RandomUtils.nextDouble(1000.0, 10000.0)
            )
        );
    }

    @Test
    public void shouldThrowAccountIsClosedIfDestinationAccountIsClosed() {
        var sourceAccountFixture = TestDataFactory.getAccount();
        var destinationAccountFixture = TestDataFactory.getClosedAccount();

        when(this.accountRepositoryMock.findById(anyLong()))
            .thenReturn(Optional.of(sourceAccountFixture))
            .thenReturn(Optional.of(destinationAccountFixture));

        Assertions.assertThrows(
            AccountIsClosedException.class,
            () -> this.accountService.internalTransfer(
                sourceAccountFixture.getId(),
                destinationAccountFixture.getId(),
                RandomUtils.nextDouble(1000.0, 10000.0)
            )
        );
    }
}
