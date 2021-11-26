package com.epam.clientinterface.controller;

import com.epam.clientinterface.entity.Role;
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
    public Role findByName(@PathVariable String name) {
        return roleService.findByName(name);
    }

}
