package com.epam.bank.clientinterface.repository;

import com.epam.bank.clientinterface.entity.Card;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findCardByNumber(String number);
}
