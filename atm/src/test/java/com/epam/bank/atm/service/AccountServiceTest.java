package com.epam.bank.atm.service;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import com.epam.bank.atm.entity.Account;
import com.epam.bank.atm.repository.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class AccountServiceTest {

    private final double positiveAmount = 578.58;
    private final double negativeAmount = -578.58;
    private final double zeroAmount = 0.00;
    private final long accountId = 1;

    AccountRepository mockRepo = Mockito.mock(AccountRepository.class);
    private final AccountService service = new AccountService(Mockito.mock(TransactionalService.class), mockRepo);

    @AfterEach
    void tearDown() {
        reset(mockRepo);
    }

    @Test
    void putMoney() {
        var accountFixture = this.getTestingAccount();

        when(mockRepo.getById(accountId)).thenReturn(accountFixture);
        when(mockRepo.putMoney(accountId, positiveAmount)).thenReturn(accountFixture.getAmount() + positiveAmount);
        Assertions.assertEquals(
            accountFixture.getAmount() + positiveAmount,
            service.putMoney(accountId, positiveAmount)
        );
    }

    @Test
    void putMoneyZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.putMoney(accountId, zeroAmount));
    }

    @Test
    void putMoneyIncorrectAmount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.putMoney(accountId, negativeAmount));
    }

    @Test
    void putFails() {
        var accountFixture = this.getTestingAccount();

        when(mockRepo.getById(accountId)).thenReturn(accountFixture);
        when(mockRepo.putMoney(accountId, positiveAmount)).thenReturn(accountFixture.getAmount());
        Assertions.assertThrows(IllegalStateException.class,
            () -> service.putMoney(accountId, positiveAmount));
    }

    @Test
    void withdrawMoney() {
        var accountFixture = this.getTestingAccount();

        when(mockRepo.getById(accountId)).thenReturn(accountFixture);
        when(mockRepo.withdrawMoney(accountId, positiveAmount)).thenReturn(accountFixture.getAmount() - positiveAmount);
        Assertions.assertEquals(
            accountFixture.getAmount() - positiveAmount,
            service.withdrawMoney(accountId, positiveAmount)
        );
    }

    @Test
    void withdrawMoneyIncorrectAmount() {
        when(mockRepo.getById(accountId)).thenReturn(this.getTestingAccount());
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.withdrawMoney(accountId, negativeAmount));
    }

    @Test
    void withdrawMoneyMoneyZero() {
        when(mockRepo.getById(accountId)).thenReturn(this.getTestingAccount());
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.withdrawMoney(accountId, zeroAmount));
    }

    @Test
    void withdrawMoreThanWeHave() {
        var accountFixture = this.getTestingAccount();
        when(mockRepo.getById(accountId)).thenReturn(accountFixture);
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> service.withdrawMoney(accountId, accountFixture.getAmount() + 100));
    }

    @Test
    void withdrawFails() {
        var accountFixture = this.getTestingAccount();

        when(mockRepo.getById(accountId)).thenReturn(accountFixture);
        when(mockRepo.withdrawMoney(accountId, positiveAmount)).thenReturn(accountFixture.getAmount());
        Assertions.assertThrows(IllegalStateException.class,
            () -> service.withdrawMoney(accountId, positiveAmount));
    }

    private Account getTestingAccount() {
        return new Account(1L, "123", true, "plan", 10000D, 1L, null);
    }

}