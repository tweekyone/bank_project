package com.epam.clientinterface.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.AccountRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountServiceCloseAccountTest {
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepositoryMock;

    @BeforeEach
    public void beforeEach() {
        this.accountService = new AccountService(this.accountRepositoryMock);
    }

    @Test
    public void shouldReturnNothingIfAccountExists() {
        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.of(this.getAccountFixture(1L)));

        this.accountService.closeAccount(1L);
    }

    @Test
    public void shouldThrowAccountNotFoundIfAccountDoesNotExist() {
        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class, () -> this.accountService.closeAccount(1L));
    }

    private Account getAccountFixture(long id) {
        return new Account(
            id, "11111111111111111111", true, Account.Plan.BASE, 10000.00, new User(), new ArrayList<>()
        );
    }
}
