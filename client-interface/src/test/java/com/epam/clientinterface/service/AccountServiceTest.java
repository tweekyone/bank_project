package com.epam.clientinterface.service;

import static org.mockito.Mockito.when;

import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.domain.exception.NotEnoughMoneyException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.AccountRepository;
import com.epam.clientinterface.repository.TransactionRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
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
        when(this.accountRepositoryMock.findById(1L)).thenReturn(Optional.of(this.getAccountFixture(1L)));
        when(this.accountRepositoryMock.findById(2L)).thenReturn(Optional.of(this.getAccountFixture(2L)));

        this.accountService.transfer(1L, 2L, 1000.00);
    }

    @Test
    public void shouldThrowAccountNotFoundIfTheSourceAccountDoesNotExist() {
        when(this.accountRepositoryMock.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class, () -> this.accountService.transfer(1L, 2L, 1000.00));
    }

    @Test
    public void shouldThrowAccountNotFoundIfTheDestinationAccountDoesNotExist() {
        when(this.accountRepositoryMock.findById(1L)).thenReturn(Optional.of(this.getAccountFixture(1L)));
        when(this.accountRepositoryMock.findById(2L)).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class, () -> this.accountService.transfer(1L, 2L, 1000.00));
    }

    @Test
    public void shouldThrowNotEnoughMoneyIfSourceAccountDoesNotHaveEnoughMoney() {
        when(this.accountRepositoryMock.findById(1L)).thenReturn(Optional.of(this.getAccountFixture(1L)));
        when(this.accountRepositoryMock.findById(2L)).thenReturn(Optional.of(this.getAccountFixture(2L)));

        Assertions.assertThrows(NotEnoughMoneyException.class, () -> this.accountService.transfer(1L, 2L, 100000.00));
    }

    private Account getAccountFixture(long id) {
        return new Account(
            id, "11111111111111111111", true, Account.Plan.BASE, 10000.00, new User(), new ArrayList<>(), null
        );
    }
}
