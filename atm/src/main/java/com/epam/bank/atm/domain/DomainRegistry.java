package com.epam.bank.atm.domain;

import com.epam.bank.atm.di.DIContainer;
import com.epam.bank.atm.repository.AccountRepository;
import com.epam.bank.atm.repository.CardRepository;

public class DomainRegistry {
    public static AccountRepository getAccountRepository() {
        return DIContainer.instance().getSingleton(AccountRepository.class);
    }

    public static CardRepository getCardRepository() {
        return DIContainer.instance().getSingleton(CardRepository.class);
    }
}
