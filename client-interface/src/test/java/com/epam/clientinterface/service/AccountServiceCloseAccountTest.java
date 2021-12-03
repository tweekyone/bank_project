package com.epam.clientinterface.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.clientinterface.controller.util.UserTestData;
import com.epam.clientinterface.domain.exception.AccountIsClosedException;
import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.entity.User;
import com.epam.clientinterface.repository.AccountRepository;
import java.util.ArrayList;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AccountServiceCloseAccountTest {
    @Mock
    private AccountRepository accountRepositoryMock;

    @InjectMocks
    private AccountService accountService;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Test
    public void shouldReturnNothingIfAccountExists() {
        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.of(this.getAccountFixture(1L)));

        this.accountService.closeAccount(1L, 1L);

        verify(this.accountRepositoryMock).save(this.accountCaptor.capture());
        Assertions.assertNotNull(this.accountCaptor.getValue().getClosedAt());
    }

    @Test
    public void shouldThrowAccountNotFoundIfAccountDoesNotExist() {
        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(AccountNotFoundException.class, () -> this.accountService.closeAccount(1L, 1L));
    }

    @Test
    public void shouldThrowAccountIsClosedIfAccountIsClosed() {
        var accountMock = this.getAccountFixture(1L);
        accountMock.close();

        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.of(accountMock));

        Assertions.assertThrows(AccountIsClosedException.class, () -> this.accountService.closeAccount(1L, 1L));
    }

    private Account getAccountFixture(long id) {
        return new Account(
            id,
            RandomStringUtils.randomNumeric(20),
            RandomUtils.nextBoolean(),
            Account.Plan.values()[RandomUtils.nextInt(0, Account.Plan.values().length)],
            RandomUtils.nextDouble(0, 10000.00),
            UserTestData.user,
            new ArrayList<>(),
            null
        );
    }
}
