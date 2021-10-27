package com.epam.clientinterface.repository;

import com.epam.clientinterface.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
