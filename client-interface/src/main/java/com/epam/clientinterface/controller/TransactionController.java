package com.epam.clientinterface.controller;

import com.epam.clientinterface.entity.Transaction;
import com.epam.clientinterface.service.TransactionService;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Validated
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping("/transactions/{userId}/{accountNumber}")
    public ResponseEntity<List<Transaction>> readTransactions(
        @PathVariable("userId") String userId,
        @PathVariable("accountNumber") String accountNumber
    ) {
        List<Transaction> transactionList = this.transactionService.readTransactions(
            Long.valueOf(userId),
            accountNumber
        );

        return new ResponseEntity<>(transactionList, HttpStatus.OK);
    }
}
