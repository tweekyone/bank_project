package com.epam.clientinterface.service;

import com.epam.clientinterface.entity.RoleEntity;
import com.epam.clientinterface.repository.RoleRepository;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleEntity save(RoleEntity roleEntity) {
        return roleRepository.save(roleEntity);
    }

    public RoleEntity findByName(String name) {
        return roleRepository.findByName(name);
    }
}
