package com.epam.clientinterface.repository;

import com.epam.clientinterface.entity.PinCounter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PinCounterRepository extends JpaRepository<PinCounter, Long> {
    PinCounter findByCardId(Long cardId);
}
