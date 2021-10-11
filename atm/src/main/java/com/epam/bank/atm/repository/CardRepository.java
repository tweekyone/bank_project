package com.epam.bank.atm.repository;

import com.epam.bank.atm.entity.Card;
import java.util.Optional;

public interface CardRepository {
    Optional<Card> getById(long id);

    Optional<Card> getByNumber(String number);
}
