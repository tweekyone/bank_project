package com.epam.clientinterface.service;

import com.epam.clientinterface.entity.Account;
import com.epam.clientinterface.repository.AccountRepository;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {
    private final AccountRepository accountRepository;

    public Account findById(Long id) {
        Optional<Account> account = accountRepository.findById(id);
        return account.isEmpty() ? null : account.get();
    }

}
