package com.epam.clientinterface.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.epam.clientinterface.FixtureFactory;
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
    public void shouldReturnNothingIfAccountsExistAndThereIsEnoughMoney() {
        var sourceAccountFixture = FixtureFactory.getAccount();

        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.of(sourceAccountFixture));
        when(this.accountRepositoryMock.findByNumber(anyString())).thenReturn(Optional.empty());

        this.accountService.externalTransfer(
            sourceAccountFixture.getId(), RandomStringUtils.randomNumeric(20), RandomUtils.nextDouble(1000.0, 10000.0)
        );
    }

    @Test
    public void shouldThrowAccountNotFoundIfTheSourceAccountDoesNotExist() {
        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(
            AccountNotFoundException.class,
            () -> this.accountService.externalTransfer(
                RandomUtils.nextLong(), RandomStringUtils.randomNumeric(20), RandomUtils.nextDouble(1000.0, 10000.0)
            )
        );
    }

    @Test
    public void shouldThrowAccountIsNotSupposedForExternalTransferDestinationAccountExists() {
        var sourceAccountFixture = FixtureFactory.getAccount();
        var destinationAccountFixture = FixtureFactory.getAccount();

        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.of(sourceAccountFixture));
        when(this.accountRepositoryMock.findByNumber(anyString())).thenReturn(Optional.of(destinationAccountFixture));

        Assertions.assertThrows(
            AccountIsNotSupposedForExternalTransferException.class,
            () -> this.accountService.externalTransfer(
                sourceAccountFixture.getId(),
                destinationAccountFixture.getNumber(),
                RandomUtils.nextDouble(1000.0, 10000.0)
            )
        );
    }

    @Test
    public void shouldThrowNotEnoughMoneyIfSourceAccountDoesNotHaveEnoughMoney() {
        var sourceAccountFixture = FixtureFactory.getAccount();

        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.of(sourceAccountFixture));
        when(this.accountRepositoryMock.findByNumber(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(
            NotEnoughMoneyException.class,
            () -> this.accountService.externalTransfer(
                sourceAccountFixture.getId(),
                RandomStringUtils.randomNumeric(20),
                RandomUtils.nextDouble(100000.0, 1000000.0)
            )
        );
    }

    @Test
    public void shouldThrowAccountIsClosedIfAccountIsClosed() {
        var sourceAccountFixture = FixtureFactory.getClosedAccount();

        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.of(sourceAccountFixture));

        Assertions.assertThrows(
            AccountIsClosedException.class,
            () -> this.accountService.externalTransfer(
                sourceAccountFixture.getId(),
                RandomStringUtils.randomNumeric(20),
                RandomUtils.nextDouble(1000.0, 10000.0)
            )
        );
    }
}
