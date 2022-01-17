package com.epam.bank.operatorinterface.service;

import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.bank.operatorinterface.entity.Account;
import com.epam.bank.operatorinterface.entity.User;
import com.epam.bank.operatorinterface.exception.AccountCanNotBeClosedException;
import com.epam.bank.operatorinterface.exception.AccountIsClosedException;
import com.epam.bank.operatorinterface.exception.AccountNotFoundException;
import com.epam.bank.operatorinterface.exception.AccountNumberGenerationTriesLimitException;
import com.epam.bank.operatorinterface.exception.UserNotFoundException;
import com.epam.bank.operatorinterface.repository.AccountRepository;
import com.epam.bank.operatorinterface.repository.UserRepository;
import java.util.Optional;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import util.TestDataFactory;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
    @Mock
    private UserRepository userRepositoryMock;

    @Mock
    private AccountRepository accountRepositoryMock;

    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void shouldCreateDefaultAccountIfUserDoesNotHaveAccounts() {
        var userFixture = TestDataFactory.getUser();

        when(userRepositoryMock.findById(userFixture.getId())).thenReturn(Optional.of(userFixture));

        var userId = userFixture.getId();
        var plan = TestDataFactory.getAccountPlan();

        accountService.create(userId, plan);

        verify(accountRepositoryMock).save(accountCaptor.capture());
        Assertions.assertThat(accountCaptor.getValue().getUser().getId()).isEqualTo(userId);
        Assertions.assertThat(accountCaptor.getValue().getNumber()).matches(StringUtils::isNumeric);
        Assertions.assertThat(accountCaptor.getValue().getNumber()).matches(number -> number.length() == 20);
        Assertions.assertThat(accountCaptor.getValue().isDefault()).isTrue();
        Assertions.assertThat(accountCaptor.getValue().getPlan()).isEqualTo(plan);
    }

    @Test
    public void shouldCreateNotDefaultAccountIfUserAlreadyHasAccounts() {
        var userFixture = TestDataFactory.getUserWithAccount();

        when(userRepositoryMock.findById(userFixture.getId())).thenReturn(Optional.of(userFixture));

        var userId = userFixture.getId();
        var plan = TestDataFactory.getAccountPlan();

        accountService.create(userId, plan);

        verify(accountRepositoryMock).save(accountCaptor.capture());
        Assertions.assertThat(accountCaptor.getValue().getUser().getId()).isEqualTo(userId);
        Assertions.assertThat(accountCaptor.getValue().getNumber()).matches(StringUtils::isNumeric);
        Assertions.assertThat(accountCaptor.getValue().getNumber()).matches(number -> number.length() == 20);
        Assertions.assertThat(accountCaptor.getValue().isDefault()).isFalse();
        Assertions.assertThat(accountCaptor.getValue().getPlan()).isEqualTo(plan);
    }

    @Test
    public void shouldThrowIfTryToCreateAccountForNotExistingUser() {
        when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        var userId = RandomUtils.nextLong();
        var plan = TestDataFactory.getAccountPlan();

        Assertions.assertThatThrownBy(() -> accountService.create(userId, plan))
            .isExactlyInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void shouldThrowIfCanNotGenerateUniqueAccountNumber() {
        var accountFixture = TestDataFactory.getAccount();
        when(userRepositoryMock.findById(accountFixture.getUser().getId()))
            .thenReturn(Optional.of(accountFixture.getUser()));
        when(accountRepositoryMock.existsByNumber(anyString())).thenReturn(true);

        var userId = accountFixture.getUser().getId();
        var plan = TestDataFactory.getAccountPlan();

        Assertions.assertThatThrownBy(() -> accountService.create(userId, plan))
            .isExactlyInstanceOf(AccountNumberGenerationTriesLimitException.class);
    }

    @Test
    public void shouldMakeDefaultNotDefaultAccount() {
        var userFixture = TestDataFactory.getUserWithSeveralAccounts(3);
        var accountFixture = userFixture.getAccounts().stream().filter(Account::isNotDefault).findAny().orElseThrow();

        assertThatUserHasOnlyThisAccountAsDefaultAfterUseCaseExecution(accountFixture);
    }

    @Test
    public void shouldMakeDefaultAlreadyDefaultAccount() {
        var userFixture = TestDataFactory.getUserWithSeveralAccounts(3);
        var accountFixture = userFixture.getAccounts().stream().filter(Account::isDefault).findAny().orElseThrow();

        assertThatUserHasOnlyThisAccountAsDefaultAfterUseCaseExecution(accountFixture);
    }

    private void assertThatUserHasOnlyThisAccountAsDefaultAfterUseCaseExecution(Account accountFixture) {
        when(accountRepositoryMock.findById(accountFixture.getId())).thenReturn(Optional.of(accountFixture));

        accountService.makeDefault(accountFixture.getId());

        verify(userRepositoryMock).save(userCaptor.capture());

        var user = userCaptor.getValue();
        Assertions.assertThat(user.getDefaultAccount().orElseThrow().getId()).isEqualTo(accountFixture.getId());
        Assertions
            .assertThat(
                user.getAccounts().stream()
                    .filter(a -> !a.getId().equals(accountFixture.getId()))
                    .filter(Account::isNotDefault)
                    .count()
            )
            .isEqualTo(user.getAccounts().size() - 1);
    }

    @Test
    public void shouldThrowIfTryToMakeDefaultNotExistingAccount() {
        when(accountRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> accountService.makeDefault(RandomUtils.nextLong()))
            .isExactlyInstanceOf(AccountNotFoundException.class);
    }

    @Test
    public void shouldThrowIfTryToMakeDefaultClosedAccount() {
        var accountFixture = TestDataFactory.getClosedAccount();

        when(accountRepositoryMock.findById(accountFixture.getId())).thenReturn(Optional.of(accountFixture));

        Assertions.assertThatThrownBy(() -> accountService.makeDefault(accountFixture.getId()))
            .isExactlyInstanceOf(AccountIsClosedException.class);
    }

    @Test
    public void shouldCloseSingleAccountOfUser() {
        assertThatAccountIsClosedAfterUseCaseExecution(TestDataFactory.getAccount());
    }

    @Test
    public void shouldCloseNotDefaultAccountOfUser() {
        var userFixture = TestDataFactory.getUserWithSeveralAccounts();
        var accountFixture = userFixture.getAccounts().stream().filter(Account::isNotDefault).findAny().orElseThrow();

        assertThatAccountIsClosedAfterUseCaseExecution(accountFixture);
    }

    private void assertThatAccountIsClosedAfterUseCaseExecution(Account accountFixture) {
        when(accountRepositoryMock.findById(accountFixture.getId())).thenReturn(Optional.of(accountFixture));

        accountService.close(accountFixture.getId());

        verify(accountRepositoryMock).save(accountCaptor.capture());
        Assertions.assertThat(accountCaptor.getValue().getId()).isEqualTo(accountFixture.getId());
        Assertions.assertThat(accountCaptor.getValue().isActive()).isFalse();
        Assertions.assertThat(accountCaptor.getValue().getClosedAt()).isNotNull();
    }

    @Test
    public void shouldThrowIfTryToCloseAlreadyClosedAccount() {
        var accountFixture = TestDataFactory.getClosedAccount();

        when(accountRepositoryMock.findById(accountFixture.getId())).thenReturn(Optional.of(accountFixture));

        Assertions.assertThatThrownBy(() -> accountService.close(accountFixture.getId()))
            .isExactlyInstanceOf(AccountIsClosedException.class);
    }

    @Test
    public void shouldThrowIfTryToCloseNotSingleAndDefaultAccountOfUser() {
        var userFixture = TestDataFactory.getUserWithSeveralAccounts();
        var accountFixture = userFixture.getDefaultAccount().orElseThrow();

        when(accountRepositoryMock.findById(accountFixture.getId())).thenReturn(Optional.of(accountFixture));

        Assertions.assertThatThrownBy(() -> accountService.close(accountFixture.getId()))
            .isExactlyInstanceOf(AccountCanNotBeClosedException.class);
    }
}
