package com.epam.clientinterface.controller;

import com.epam.clientinterface.entity.RoleEntity;
import com.epam.clientinterface.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping(path = "/{name}")
    public RoleEntity findByName(@PathVariable String name) {
        return roleService.findByName(name);
    }

    // TODO Don't do that, just for testing in browser
    @GetMapping(path = "/create/{name}")
    public RoleEntity create(@PathVariable String name) {
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setName(name);
        return roleService.save(roleEntity);
    }
}
