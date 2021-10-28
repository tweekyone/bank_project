package com.epam.clientinterface.service;

import com.epam.clientinterface.domain.exception.AccountNotFoundException;
import com.epam.clientinterface.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public void closeAccount(long accountId) {
        this.accountRepository.delete(this.accountRepository.findById(accountId).orElseThrow(() -> {
            throw new AccountNotFoundException(accountId);
        }));
    }
}
