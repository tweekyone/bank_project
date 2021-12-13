package com.epam.clientinterface.service;

import static com.epam.clientinterface.util.TestDataFactory.getAccount;
import static com.epam.clientinterface.util.TestDataFactory.getAccountBelongsToUser;
import static com.epam.clientinterface.util.TestDataFactory.getClosedAccountBelongsToUser;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

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
    void shouldReturnNothingIfAccountsExistAndThereIsEnoughMoney() {
        var sourceAccountFixture = getAccountBelongsToUser();
        var destinationAccountFixture = getAccountBelongsToUser();

        when(accountRepositoryMock.findAccountByIdWithUser(anyLong(), anyLong()))
            .thenReturn(Optional.of(sourceAccountFixture))
            .thenReturn(Optional.of(destinationAccountFixture));

        accountService.internalTransfer(sourceAccountFixture.getId(),
            destinationAccountFixture.getId(),
            RandomUtils.nextDouble(1000.0, 10000.0), 1);
    }

    @Test
    void shouldThrowAccountNotFoundIfTheSourceAccountDoesNotExist() {
        Assertions.assertThrows(
            AccountNotFoundException.class,
            () -> this.accountService.internalTransfer(RandomUtils.nextLong(),
                RandomUtils.nextLong(),
                RandomUtils.nextDouble(1000.0, 10000.0),  1)
        );
    }

    @Test
    void shouldThrowAccountNotFoundIfTheDestinationAccountDoesNotExist() {
        var sourceAccountFixture = getAccount();

        Assertions.assertThrows(
            AccountNotFoundException.class,
            () -> this.accountService.internalTransfer(sourceAccountFixture.getId(),
                RandomUtils.nextLong(),
                RandomUtils.nextDouble(1000.0, 10000.0), 1)
        );
    }

    @Test
    void shouldThrowNotEnoughMoneyIfSourceAccountDoesNotHaveEnoughMoney() {
        var sourceAccountFixture = getAccountBelongsToUser();
        var destinationAccountFixture = getAccountBelongsToUser();

        when(this.accountRepositoryMock.findAccountByIdWithUser(anyLong(), anyLong()))
            .thenReturn(Optional.of(sourceAccountFixture))
            .thenReturn(Optional.of(destinationAccountFixture));

        Assertions.assertThrows(
            NotEnoughMoneyException.class,
            () -> this.accountService.internalTransfer(
                sourceAccountFixture.getId(),
                destinationAccountFixture.getId(),
                RandomUtils.nextDouble(100000.0, 1000000.0), 1
            )
        );
    }

    @Test
    void shouldThrowAccountIsClosedIfSourceAccountIsClosed() {
        var sourceAccountFixture = getClosedAccountBelongsToUser();
        var destinationAccountFixture = getAccountBelongsToUser();

        when(accountRepositoryMock.findAccountByIdWithUser(anyLong(), anyLong()))
            .thenReturn(Optional.of(sourceAccountFixture))
            .thenReturn(Optional.of(destinationAccountFixture));

        Assertions.assertThrows(
            AccountIsClosedException.class,
            () -> this.accountService.internalTransfer(
                sourceAccountFixture.getId(),
                destinationAccountFixture.getId(),
                RandomUtils.nextDouble(1000.0, 10000.0), 1
            )
        );
    }

    @Test
    void shouldThrowAccountIsClosedIfDestinationAccountIsClosed() {
        var sourceAccountFixture = getAccountBelongsToUser();
        var destinationAccountFixture = getClosedAccountBelongsToUser();

        when(accountRepositoryMock.findAccountByIdWithUser(anyLong(), anyLong()))
            .thenReturn(Optional.of(sourceAccountFixture))
            .thenReturn(Optional.of(destinationAccountFixture));

        Assertions.assertThrows(
            AccountIsClosedException.class,
            () -> accountService.internalTransfer(
                sourceAccountFixture.getId(),
                destinationAccountFixture.getId(),
                RandomUtils.nextDouble(1000.0, 10000.0), 1
            )
        );
    }

    @Test
    void shouldThrowAccountNotFound_IfAccountDoesNotBelongToUsers() {
        Assertions.assertThrows(AccountNotFoundException.class,
            () -> accountService.internalTransfer(getAccount().getId(), getAccount().getId(), 100, 1));
    }
}
