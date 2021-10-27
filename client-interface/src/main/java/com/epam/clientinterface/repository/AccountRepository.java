package com.epam.clientinterface.repository;

import com.epam.clientinterface.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
