package com.epam.clientinterface.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "bank/secured")
public class UserController {

    @GetMapping("hello")
    public String hello() {
        return "Hello";
    }
}
