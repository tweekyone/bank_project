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

    @Query("SELECT a FROM Account a JOIN FETCH a.user WHERE a.id = ?1 AND a.user.id=?2")
    Optional<Account> findAccountByIdWithUser(long accountId, long userId);

    Account save(Account account);

    @Query("SELECT number FROM Account WHERE user_id = ?1")
    List<String> findAccountsByUserId(Long userId);
}
