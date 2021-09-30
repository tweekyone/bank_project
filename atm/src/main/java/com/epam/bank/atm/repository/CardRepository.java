package com.epam.bank.atm.repository;

import com.epam.bank.atm.entity.Card;

public interface CardRepository {
    Card getById(long id);
    Card getByNumber(String number);
}
