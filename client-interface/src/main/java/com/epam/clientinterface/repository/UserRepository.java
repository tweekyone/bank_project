package com.epam.clientinterface.repository;

import com.epam.clientinterface.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User save(User user);

    Optional<User> getByUsername(String username);

    boolean findByEmail(String email);
}
