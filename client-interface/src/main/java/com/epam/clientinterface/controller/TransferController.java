package com.epam.clientinterface.controller;

import com.epam.clientinterface.configuration.security.SecurityUtil;
import com.epam.clientinterface.controller.dto.request.ExternalTransferRequest;
import com.epam.clientinterface.controller.dto.request.InternalTransferRequest;
import com.epam.clientinterface.service.AccountService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transfer")
public class TransferController {
    private final AccountService accountService;

    @PostMapping("/internal")
    public ResponseEntity<?> internalTransfer(@Valid @RequestBody InternalTransferRequest request) {
        long userId = SecurityUtil.authUserId();
        this.accountService.internalTransfer(
            request.getSourceAccountId(),
            request.getDestinationAccountId(),
            request.getAmount(), userId
        );

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/external")
    public ResponseEntity<?> externalTransfer(@Valid @RequestBody ExternalTransferRequest request) {
        long userId = SecurityUtil.authUserId();
        this.accountService.externalTransfer(
            request.getSourceAccountId(),
            request.getDestinationAccountNumber(),
            request.getAmount(), userId
        );

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
