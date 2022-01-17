package com.epam.bank.operatorinterface.repository;

import com.epam.bank.operatorinterface.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
