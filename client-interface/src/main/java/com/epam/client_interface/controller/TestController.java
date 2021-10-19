package com.epam.client_interface.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TestController {

    @Value("${app.testValue}")
    private String testValue;

    @GetMapping(path = "/")
    public String hello() {
        log.info("test");
        return testValue;
    }
}
