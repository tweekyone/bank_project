package com.epam.clientinterface.repository;

import com.epam.clientinterface.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);
    Optional<User> getByUsername(String username);
    boolean findByEmail(String email);
}
