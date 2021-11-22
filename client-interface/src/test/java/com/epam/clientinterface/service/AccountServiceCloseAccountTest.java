package com.epam.clientinterface.service;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.clientinterface.FixtureFactory;
import com.epam.clientinterface.domain.exception.AccountIsClosedException;
import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.repository.AccountRepository;
import java.util.Optional;
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
class AccountServiceCloseAccountTest {
    @Mock
    private AccountRepository accountRepositoryMock;

    @InjectMocks
    private AccountService accountService;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Test
    public void shouldReturnNothingIfAccountExists() {
        var accountFixture = FixtureFactory.getAccount();

        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.of(accountFixture));

        this.accountService.closeAccount(accountFixture.getId());

        verify(this.accountRepositoryMock).save(this.accountCaptor.capture());
        Assertions.assertNotNull(this.accountCaptor.getValue().getClosedAt());
    }

    @Test
    public void shouldThrowAccountNotFoundIfAccountDoesNotExist() {
        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(
            AccountNotFoundException.class,
            () -> this.accountService.closeAccount(RandomUtils.nextLong())
        );
    }

    @Test
    public void shouldThrowAccountIsClosedIfAccountIsClosed() {
        var accountFixture = FixtureFactory.getClosedAccount();

        when(this.accountRepositoryMock.findById(anyLong())).thenReturn(Optional.of(accountFixture));

        Assertions.assertThrows(
            AccountIsClosedException.class,
            () -> this.accountService.closeAccount(accountFixture.getId())
        );
    }
}
