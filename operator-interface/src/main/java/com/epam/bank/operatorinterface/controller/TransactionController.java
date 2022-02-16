package com.epam.bank.operatorinterface.controller;

import com.epam.bank.operatorinterface.entity.Transaction;
import com.epam.bank.operatorinterface.service.TransactionService;
import com.epam.bank.operatorinterface.service.importpkg.TransactionImportType;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/{transactionId}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable("transactionId") Long transactionId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        return new ResponseEntity<>(transaction, HttpStatus.OK);
    }

    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void importCsvTransactions(@RequestParam TransactionImportType type,
                                      @RequestParam MultipartFile file) throws IOException {
        transactionService.importTransactions(type, file.getInputStream());
    }

}
