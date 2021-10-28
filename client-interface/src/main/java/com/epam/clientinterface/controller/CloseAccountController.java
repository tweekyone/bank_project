package com.epam.clientinterface.controller;

import com.epam.clientinterface.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CloseAccountController {
    private final AccountService accountService;

    @DeleteMapping("/accounts/{accountId}")
    public ResponseEntity<?> closeAccount(@PathVariable long accountId) {
        this.accountService.closeAccount(accountId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
