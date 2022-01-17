package com.epam.bank.operatorinterface.repository;

import com.epam.bank.operatorinterface.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CardRepository extends JpaRepository<Card, Long> {
    @Modifying
    @Query("update Card set pinCounter = 0")
    void resetPinCounter();
}
