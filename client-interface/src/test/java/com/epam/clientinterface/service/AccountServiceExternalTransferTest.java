package com.epam.clientinterface.service;

import com.epam.clientinterface.domain.exception.AccountIsNotSupposedForExternalTransferException;
import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.domain.exception.NotEnoughMoneyException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.AccountRepository;
import com.epam.clientinterface.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceExternalTransferTest {
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
        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.of(this.getAccountFixture(1L)));
        when(this.accountRepositoryMock.findByNumber(anyString())).thenReturn(Optional.empty());

        this.accountService.externalTransfer(1L, "123", 1000.00);
    }

    @Test
    public void shouldThrowAccountNotFoundIfTheSourceAccountDoesNotExist() {
        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(
            AccountNotFoundException.class,
            () -> this.accountService.externalTransfer(1L, "123", 1000.00)
        );
    }

    @Test
    public void shouldThrowAccountIsNotSupposedForExternalTransferDestinationAccountExists() {
        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.of(this.getAccountFixture(1L)));
        when(this.accountRepositoryMock.findByNumber(anyString())).thenReturn(Optional.of(this.getAccountFixture(2L)));

        Assertions.assertThrows(
            AccountIsNotSupposedForExternalTransferException.class,
            () -> this.accountService.externalTransfer(1L, "123", 1000.00)
        );
    }

    @Test
    public void shouldThrowNotEnoughMoneyIfSourceAccountDoesNotHaveEnoughMoney() {
        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.of(this.getAccountFixture(1L)));
        when(this.accountRepositoryMock.findByNumber(anyString())).thenReturn(Optional.empty());

        Assertions.assertThrows(
            NotEnoughMoneyException.class,
            () -> this.accountService.externalTransfer(1L, "123", 100000.00)
        );
    }

    private Account getAccountFixture(long id) {
        return new Account(
            id, "11111111111111111111", true, Account.Plan.BASE, 10000.00, new User(), new ArrayList<>()
        );
    }
}
