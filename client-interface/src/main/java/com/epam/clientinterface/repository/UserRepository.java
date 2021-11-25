package com.epam.clientinterface.repository;

import com.epam.clientinterface.domain.exception.NotFoundException;
import com.epam.clientinterface.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    default User getById(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(User.class, id));
    }

    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = {"roles"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT u FROM User u WHERE u.email=?1")
    Optional<User> findByEmailWithRoles(String email);
}
