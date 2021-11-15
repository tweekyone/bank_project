package com.epam.clientinterface.controller;

import com.epam.clientinterface.controller.dto.request.ReadTransactionsRequest;
import com.epam.clientinterface.entity.Transaction;
import com.epam.clientinterface.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    @PostMapping("/read-transactions")
    public ResponseEntity<List<Transaction>> readTransactions(@Valid @RequestBody ReadTransactionsRequest request) {
        List<Transaction> transactionList = this.transactionService.readTransactions(
            request.getUserId(),
            request.getSourceAccountNumber()
        );

        return new ResponseEntity<>(transactionList, HttpStatus.OK);
    }
}
