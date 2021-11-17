package com.epam.clientinterface.repository;

import com.epam.clientinterface.entity.Account;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByNumber(String number);

    @Query("SELECT number FROM Account WHERE user_id = ?1")
    List<String> findAccountsByUserId(Long userId);
}
