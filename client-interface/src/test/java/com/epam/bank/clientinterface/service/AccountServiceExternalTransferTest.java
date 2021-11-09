package com.epam.bank.clientinterface.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.epam.bank.clientinterface.domain.exception.AccountIsNotSupposedForExternalTransferException;
import com.epam.bank.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.bank.clientinterface.domain.exception.NotEnoughMoneyException;
import com.epam.bank.clientinterface.entity.Account;
import com.epam.bank.clientinterface.entity.User;
import com.epam.bank.clientinterface.repository.AccountRepository;
import com.epam.bank.clientinterface.repository.TransactionRepository;
import java.util.ArrayList;
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
public class AccountServiceExternalTransferTest {
    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepositoryMock;

    @Mock
    private TransactionRepository transactionRepositoryMock;

    @Test
    public void shouldReturnNothingIfAccountsExistAndThereIsEnoughMoney() {
        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.of(this.getAccountFixture(1L)));
        when(this.accountRepositoryMock.findByNumber(anyString())).thenReturn(Optional.empty());

        this.accountService.externalTransfer(
            1L, RandomStringUtils.randomNumeric(20), RandomUtils.nextDouble(1000.0, 10000.0)
        );
    }

    @Test
    public void shouldThrowAccountNotFoundIfTheSourceAccountDoesNotExist() {
        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(
            AccountNotFoundException.class,
            () -> this.accountService.externalTransfer(
                1L, RandomStringUtils.randomNumeric(20), RandomUtils.nextDouble(1000.0, 10000.0)
            )
        );
    }

    @Test
    public void shouldThrowAccountIsNotSupposedForExternalTransferDestinationAccountExists() {
        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.of(this.getAccountFixture(1L)));
        when(this.accountRepositoryMock.findByNumber(anyString())).thenReturn(Optional.of(this.getAccountFixture(2L)));

        Assertions.assertThrows(
            AccountIsNotSupposedForExternalTransferException.class,
            () -> this.accountService.externalTransfer(
                1L, RandomStringUtils.randomNumeric(20), RandomUtils.nextDouble(1000.0, 10000.0)
            )
        );
    }

    @Test
    public void shouldThrowNotEnoughMoneyIfSourceAccountDoesNotHaveEnoughMoney() {
        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.of(this.getAccountFixture(1L)));
        when(this.accountRepositoryMock.findByNumber(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(
            NotEnoughMoneyException.class,
            () -> this.accountService.externalTransfer(
                1L, RandomStringUtils.randomNumeric(20), RandomUtils.nextDouble(100000.0, 1000000.0)
            )
        );
    }

    private Account getAccountFixture(long id) {
        return new Account(
            id,
            RandomStringUtils.randomNumeric(20),
            RandomUtils.nextBoolean(),
            Account.Plan.values()[RandomUtils.nextInt(0, Account.Plan.values().length)],
            RandomUtils.nextDouble(10000.0, 100000.0),
            new User(),
            new ArrayList<>()
        );
    }
}
