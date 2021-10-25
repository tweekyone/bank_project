package com.epam.clientinterface.repository;

import com.epam.clientinterface.entity.CardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<CardEntity, Long> {
    CardEntity findByNumber(String number);
}
