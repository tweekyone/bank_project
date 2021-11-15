package com.epam.clientinterface.service;

import com.epam.clientinterface.entity.Transaction;
import com.epam.clientinterface.repository.TransactionRepository;
import com.epam.clientinterface.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public List<Transaction> readTransactions(Long userId, String sourceAccountNumber) {
        return transactionRepository.getTransactionsByNumber(sourceAccountNumber);
    }
}
