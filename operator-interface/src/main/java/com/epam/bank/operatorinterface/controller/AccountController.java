package com.epam.bank.operatorinterface.controller;

import com.epam.bank.operatorinterface.controller.dto.request.CreateAccountRequest;
import com.epam.bank.operatorinterface.controller.dto.response.AccountResponse;
import com.epam.bank.operatorinterface.controller.mapper.AccountMapper;
import com.epam.bank.operatorinterface.service.AccountService;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@AllArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper responseMapper;

    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody CreateAccountRequest request) {
        return new ResponseEntity<>(
            responseMapper.map(accountService.create(request.getUserId(), request.getPlan())),
            HttpStatus.CREATED
        );
    }

    @PatchMapping("/{accountId}/set-as-default")
    public ResponseEntity<?> makeDefault(@PathVariable long accountId) {
        accountService.makeDefault(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{accountId}")
    public ResponseEntity<?> close(@PathVariable long accountId) {
        accountService.close(accountId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
