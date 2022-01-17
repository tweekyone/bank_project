package com.epam.bank.operatorinterface.service;

import com.epam.bank.operatorinterface.entity.Account;
import com.epam.bank.operatorinterface.entity.AccountPlan;
import com.epam.bank.operatorinterface.exception.AccountCanNotBeClosedException;
import com.epam.bank.operatorinterface.exception.AccountIsClosedException;
import com.epam.bank.operatorinterface.exception.AccountNotFoundException;
import com.epam.bank.operatorinterface.exception.AccountNumberGenerationTriesLimitException;
import com.epam.bank.operatorinterface.exception.UserNotFoundException;
import com.epam.bank.operatorinterface.repository.AccountRepository;
import com.epam.bank.operatorinterface.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public Account create(long userId, AccountPlan plan) {
        var user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        var isDefault = user.getActiveAccounts().size() == 0;
        var account = new Account(user, generateNumber(), isDefault, plan);

        return accountRepository.save(account);
    }

    private String generateNumber() {
        String number;
        var triesLimit = 100;
        do {
            number = RandomStringUtils.randomNumeric(20);
            triesLimit--;
        } while (accountRepository.existsByNumber(number) && triesLimit > 0);

        if (accountRepository.existsByNumber(number)) {
            throw new AccountNumberGenerationTriesLimitException();
        }

        return number;
    }

    @Transactional
    public void makeDefault(long accountId) {
        var account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));

        assertAccountIsNotClosed(account);

        account.getUser().getAccounts().forEach(Account::makeNotDefault);
        account.makeDefault();

        userRepository.save(account.getUser());
    }

    public void close(long accountId) {
        var account = accountRepository.findById(accountId).orElseThrow(() -> new AccountNotFoundException(accountId));

        assertAccountIsNotClosed(account);
        assertAccountCanBeClosed(account);

        account.close();

        accountRepository.save(account);
    }

    public static void assertAccountIsNotClosed(Account account) {
        if (!account.isActive()) {
            throw new AccountIsClosedException(account.getId());
        }
    }

    private static void assertAccountCanBeClosed(Account account) {
        if (account.isDefault() && account.isActive() && account.getUser().getActiveAccounts().size() > 1) {
            throw new AccountCanNotBeClosedException(account.getId());
        }
    }
}
