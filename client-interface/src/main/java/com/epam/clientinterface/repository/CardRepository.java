package com.epam.clientinterface.repository;

import com.epam.clientinterface.entity.Card;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    Optional<Card> findCardByNumber(String number);

    @Modifying
    @Query("update Card set pinCounter = 0")
    int dropPinCounter();
}
