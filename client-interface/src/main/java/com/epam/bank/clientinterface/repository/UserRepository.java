package com.epam.bank.clientinterface.repository;

import com.epam.bank.clientinterface.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
