package com.epam.bank.atm.service;

import com.epam.bank.atm.repository.AccountRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

public class AccountServiceTest {

    private final double currentAmount = 5678.58;
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
        when(mockRepo.getCurrentAmount(accountId)).thenReturn(currentAmount);
        when(mockRepo.putMoney(accountId, positiveAmount)).thenReturn(currentAmount + positiveAmount);
        Assertions.assertEquals(currentAmount + positiveAmount, service.putMoney(accountId, positiveAmount));
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
        when(mockRepo.getCurrentAmount(accountId)).thenReturn(currentAmount);
        when(mockRepo.putMoney(accountId, positiveAmount)).thenReturn(currentAmount);
        Assertions.assertThrows(IllegalStateException.class,
            () -> service.putMoney(accountId, positiveAmount));
    }

    @Test
    void withdrawMoney() {
        when(mockRepo.getCurrentAmount(accountId)).thenReturn(currentAmount);
        when(mockRepo.withdrawMoney(accountId, positiveAmount)).thenReturn(currentAmount - positiveAmount);
        Assertions.assertEquals(currentAmount - positiveAmount, service.withdrawMoney(accountId, positiveAmount));
    }

    @Test
    void withdrawMoneyIncorrectAmount() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.withdrawMoney(accountId, negativeAmount));
    }

    @Test
    void withdrawMoneyMoneyZero() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.withdrawMoney(accountId, zeroAmount));
    }

    @Test
    void withdrawMoreThanWeHave() {
        Assertions.assertThrows(IllegalArgumentException.class,
            () -> service.withdrawMoney(accountId, currentAmount + 100));
    }

    @Test
    void withdrawFails() {
        when(mockRepo.getCurrentAmount(accountId)).thenReturn(currentAmount);
        when(mockRepo.withdrawMoney(accountId, positiveAmount)).thenReturn(currentAmount);
        Assertions.assertThrows(IllegalStateException.class,
            () -> service.withdrawMoney(accountId, positiveAmount));
    }

}