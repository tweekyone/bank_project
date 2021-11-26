package com.epam.clientinterface.service;

import com.epam.clientinterface.domain.RoleAuth;
import com.epam.clientinterface.entity.Role;
import com.epam.clientinterface.repository.RoleRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public Role findByName(String name) {
        return roleRepository.findByAuthority(name);
    }
}
