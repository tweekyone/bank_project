package com.epam.clientinterface.repository;

import com.epam.clientinterface.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByAuthority(String name);
}
