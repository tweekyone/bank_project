package com.epam.bank.operatorinterface.controller;

import com.epam.bank.operatorinterface.controller.dto.request.ExternalTransferRequest;
import com.epam.bank.operatorinterface.controller.dto.request.InternalTransferRequest;
import com.epam.bank.operatorinterface.service.AccountService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
@RequiredArgsConstructor
public class TransferController {
    private final AccountService accountService;

    @PostMapping("/internal")
    @ResponseStatus(HttpStatus.CREATED)
    public void internalTransfer(@Valid @RequestBody InternalTransferRequest request) {
        var destinationAccountNumber = request.getDestinationAccountNumber();
        var destinationCardNumber = request.getDestinationCardNumber();

        if (destinationAccountNumber != null) {
            this.accountService.internalTransferByAccount(
                request.getSourceAccountId(),
                destinationAccountNumber,
                request.getAmount()
            );
        } else {
            this.accountService.internalTransferByCard(
                request.getSourceAccountId(),
                destinationCardNumber,
                request.getAmount()
            );
        }
    }

    @PostMapping("/external")
    @ResponseStatus(HttpStatus.CREATED)
    public void externalTransfer(@Valid @RequestBody ExternalTransferRequest request) {
        this.accountService.externalTransferByAccount(
            request.getSourceAccountId(),
            request.getDestinationAccountNumber(),
            request.getAmount()
        );
    }
}
