package com.epam.bank.atm.domain;

import com.epam.bank.atm.di.DiContainer;
import com.epam.bank.atm.repository.AccountRepository;
import com.epam.bank.atm.repository.CardRepository;

public class DomainRegistry {
    public static AccountRepository getAccountRepository() {
        return DiContainer.instance().getSingleton(AccountRepository.class);
    }

    public static CardRepository getCardRepository() {
        return DiContainer.instance().getSingleton(CardRepository.class);
    }
}
