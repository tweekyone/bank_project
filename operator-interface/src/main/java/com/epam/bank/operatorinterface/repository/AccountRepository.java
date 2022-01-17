package com.epam.bank.operatorinterface.repository;

import com.epam.bank.operatorinterface.entity.Account;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @NonNull Account save(@NonNull Account account);

    boolean existsByNumber(String number);
}
