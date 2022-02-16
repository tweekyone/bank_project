package com.epam.bank.operatorinterface.service;

import com.epam.bank.operatorinterface.entity.Transaction;
import com.epam.bank.operatorinterface.exception.TransactionNotFoundException;
import com.epam.bank.operatorinterface.repository.TransactionRepository;
import com.epam.bank.operatorinterface.service.importpkg.TransactionImportType;
import com.epam.bank.operatorinterface.service.importpkg.TransactionImporterCsv;
import com.epam.bank.operatorinterface.util.DateFormatter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionImporterCsv importerCsv;

    public Transaction getTransactionById(Long transactionId) {
        Optional<Transaction> transaction = transactionRepository.findById(transactionId);

        return transaction
            .orElseThrow(() -> new TransactionNotFoundException(transactionId)
            );
    }

    public List<Transaction> getAllTransactionsByAccountNumber(String accountNumber) {
        List<Transaction> transactions =
            transactionRepository
                .findBySourceAccountAccountNumberOrDestinationAccountAccountNumber(
                    accountNumber, accountNumber);
        isTransactionsExist(transactions, accountNumber);

        return transactions;
    }

    public List<Transaction> getAccountTransactionsByDateTime(String accountNumber, ZonedDateTime dateTime) {
        List<Transaction> transactions =
            transactionRepository
                .findBySourceAccountAccountNumberAndDateTimeOrDestinationAccountAccountNumberAndDateTime(
                    accountNumber, dateTime, accountNumber, dateTime);
        isTransactionsExist(transactions, accountNumber);

        return transactions;
    }

    public List<Transaction> getAccountTransactionsByDate(String accountNumber, LocalDate date) {
        List<Transaction> transactions =
            transactionRepository.findAccountTransactionsByDate(accountNumber, date);
        isTransactionsExist(transactions, accountNumber);

        return transactions;
    }

    public List<Transaction> getAccountTransactionsByYearMonth(String accountNumber, YearMonth yearMonth) {
        List<Transaction> transactions =
            transactionRepository.findAccountTransactionsByYearMonth(
                accountNumber, yearMonth.format(DateFormatter.YEAR_MONTH)
            );
        isTransactionsExist(transactions, accountNumber);

        return transactions;
    }

    public List<Transaction> getAccountTransactionsByPeriod(String accountNumber,
                                                            LocalDate from, LocalDate to) {
        List<Transaction> transactions =
            transactionRepository.findAccountTransactionsFromToDates(accountNumber, from, to);
        isTransactionsExist(transactions, accountNumber);

        return transactions;
    }

    @Transactional
    public void importTransactions(TransactionImportType type, InputStream inputStream) throws IOException {
        if (type == TransactionImportType.CSV) {
            List<Transaction> transactions = importerCsv.importCsv(inputStream);
            transactionRepository.saveAllAndFlush(transactions);
        }
    }

    private void isTransactionsExist(List<Transaction> transactions, String accountNumber) {
        if (transactions.isEmpty()) {
            throw new TransactionNotFoundException(accountNumber);
        }
    }

}
