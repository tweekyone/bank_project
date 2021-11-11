package com.epam.clientinterface.controller;

import com.epam.clientinterface.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> closeAccount(@PathVariable long accountId) {
        this.accountService.closeAccount(accountId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
