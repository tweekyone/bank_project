package com.epam.bank.operatorinterface.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

import com.epam.bank.operatorinterface.entity.Transaction;
import com.epam.bank.operatorinterface.entity.TransactionAccountData;
import com.epam.bank.operatorinterface.enumerated.TransactionOperationType;
import com.epam.bank.operatorinterface.enumerated.TransactionState;
import com.epam.bank.operatorinterface.exception.TransactionNotFoundException;
import com.epam.bank.operatorinterface.repository.TransactionRepository;
import com.epam.bank.operatorinterface.util.DateFormatter;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    private final double amount = RandomUtils.nextDouble();
    private final long transactionId = RandomUtils.nextLong();
    private final String accountNumber = RandomStringUtils.randomNumeric(20);
    private final ZonedDateTime dateTime = ZonedDateTime.now().withFixedOffsetZone();

    private final TransactionAccountData sourceAccount =
        new TransactionAccountData(RandomStringUtils.randomNumeric(20), RandomUtils.nextBoolean());

    private final TransactionAccountData destinationAccount =
        new TransactionAccountData(RandomStringUtils.randomNumeric(20), RandomUtils.nextBoolean());

    private final TransactionOperationType operationType =
        getRandomEnumType(TransactionOperationType.values());

    private final TransactionState transactionState =
        getRandomEnumType(TransactionState.values());

    private final Transaction transactionMock =
        new Transaction(transactionId, sourceAccount, destinationAccount,
            amount, dateTime, operationType, transactionState);

    private final List<Transaction> expectedTransactions = getTransactionsMock(transactionMock);

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldReturnTransactionById() {
        Mockito.when(transactionRepository.findById(transactionId))
            .thenReturn(Optional.of(transactionMock));

        Transaction resultTransaction = transactionService.getTransactionById(transactionId);

        Assertions.assertThat(resultTransaction)
            .isEqualTo(transactionMock);

        Mockito.verify(transactionRepository).findById(anyLong());
    }

    @Test
    void shouldReturnAllTransactionsByAccountNumber() {
        Mockito.when(transactionRepository
            .findBySourceAccountAccountNumberOrDestinationAccountAccountNumber(accountNumber, accountNumber))
            .thenReturn(expectedTransactions);

        List<Transaction> resultTransactions =
            transactionService.getAllTransactionsByAccountNumber(accountNumber);

        Assertions.assertThat(resultTransactions)
            .usingRecursiveComparison()
            .isEqualTo(expectedTransactions);

        Mockito.verify(transactionRepository).findBySourceAccountAccountNumberOrDestinationAccountAccountNumber(
            anyString(), anyString());
    }

    @Test
    void shouldReturnAccountTransactionsByDateTime() {
        Mockito.when(transactionRepository
            .findBySourceAccountAccountNumberAndDateTimeOrDestinationAccountAccountNumberAndDateTime(
                accountNumber, dateTime, accountNumber, dateTime))
            .thenReturn(expectedTransactions);

        List<Transaction> resultTransactions =
            transactionService.getAccountTransactionsByDateTime(accountNumber, dateTime);

        Assertions.assertThat(resultTransactions)
            .usingRecursiveComparison()
            .isEqualTo(expectedTransactions);

        Mockito.verify(transactionRepository)
            .findBySourceAccountAccountNumberAndDateTimeOrDestinationAccountAccountNumberAndDateTime(
                anyString(), any(), anyString(), any());
    }

    @Test
    void shouldReturnAccountTransactionsByDate() {
        final var date = LocalDate.now();

        Mockito.when(transactionRepository.findAccountTransactionsByDate(accountNumber, date))
            .thenReturn(expectedTransactions);

        List<Transaction> resultTransactions =
            transactionService.getAccountTransactionsByDate(accountNumber, date);

        Assertions.assertThat(resultTransactions)
            .usingRecursiveComparison()
            .isEqualTo(expectedTransactions);

        Mockito.verify(transactionRepository).findAccountTransactionsByDate(anyString(), any());
    }

    @Test
    void shouldReturnAccountTransactionsByYearMonth() {
        final var yearMonth = YearMonth.now();
        final var formatter = DateFormatter.YEAR_MONTH;

        Mockito.when(transactionRepository
            .findAccountTransactionsByYearMonth(accountNumber, yearMonth.format(formatter)))
            .thenReturn(expectedTransactions);

        List<Transaction> resultTransactions =
            transactionService.getAccountTransactionsByYearMonth(accountNumber, yearMonth);

        Assertions.assertThat(resultTransactions)
            .usingRecursiveComparison()
            .isEqualTo(expectedTransactions);

        Mockito.verify(transactionRepository).findAccountTransactionsByYearMonth(anyString(), anyString());
    }

    @Test
    void shouldThrowTransactionNotFound_WhenGetAllTransactionsByAccountNumber() {
        Optional<Transaction> emptyTransaction = Optional.empty();

        Mockito.when(transactionRepository.findById((anyLong())))
            .thenReturn(emptyTransaction);

        Assertions.assertThatThrownBy(() -> {
            transactionService.getTransactionById(transactionId);
        })
            .isInstanceOf(TransactionNotFoundException.class)
            .hasMessageContaining(String.valueOf(transactionId));

        Mockito.verify(transactionRepository).findById(anyLong());
    }

    private static <T> T getRandomEnumType(T[] array) {
        Random generator = new Random();
        int randomIndex = generator.nextInt(array.length);
        return array[randomIndex];
    }

    private List<Transaction> getTransactionsMock(Transaction transaction) {
        List<Transaction> transactionList = new ArrayList<>();
        transactionList.add(transaction);
        return transactionList;
    }
}