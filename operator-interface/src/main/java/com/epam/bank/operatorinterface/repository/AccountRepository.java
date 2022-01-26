package com.epam.bank.operatorinterface.repository;

import com.epam.bank.operatorinterface.entity.Account;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByNumber(String number);

    @Query(value = "SELECT a FROM Account a JOIN Card c ON a.id = c.account.id WHERE c.number = ?1")
    Optional<Account> findAccountByCardNumber(String cardNumber);

    @NonNull Account save(@NonNull Account account);

    boolean existsByNumber(String number);
}
