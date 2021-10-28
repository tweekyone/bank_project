package com.epam.clientinterface.controller;

import com.epam.clientinterface.controller.dto.request.ExternalTransferRequest;
import com.epam.clientinterface.service.AccountService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExternalTransferController {
    private final AccountService accountService;

    @PostMapping("/transfer/external")
    public ResponseEntity<?> externalTransfer(@Valid @RequestBody ExternalTransferRequest request) {
        this.accountService.externalTransfer(
            request.getSourceAccountId(),
            request.getDestinationAccountNumber(),
            request.getAmount()
        );

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

