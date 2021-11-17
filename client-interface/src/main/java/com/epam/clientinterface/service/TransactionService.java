package com.epam.clientinterface.service;

import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.entity.Transaction;
import com.epam.clientinterface.repository.AccountRepository;
import com.epam.clientinterface.repository.TransactionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public List<Transaction> readTransactions(Long userId, String accountNumber) {
        List<String> accountNumbers = accountRepository.findAccountsByUserId(userId);

        if (!accountNumbers.contains(accountNumber)) {
            throw new AccountNotFoundException(userId, accountNumber);
        }

        return transactionRepository.getTransactionsByNumber(accountNumber);
    }
}
