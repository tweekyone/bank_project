package com.epam.bank.clientinterface.repository;

import com.epam.bank.clientinterface.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByNumber(String number);
}
