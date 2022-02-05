package com.epam.bank.operatorinterface.controller;

import com.epam.bank.operatorinterface.controller.dto.request.CreateAccountRequest;
import com.epam.bank.operatorinterface.controller.dto.response.AccountResponse;
import com.epam.bank.operatorinterface.controller.mapper.AccountMapper;
import com.epam.bank.operatorinterface.entity.Transaction;
import com.epam.bank.operatorinterface.service.AccountService;
import com.epam.bank.operatorinterface.service.TransactionService;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    private final AccountMapper responseMapper;
    private final TransactionService transactionService;

    @GetMapping(value = "/{accountNumber}/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactionsByAccountNumber(
        @PathVariable("accountNumber")
        @Size(min = 20, max = 20) String accountNumber
    ) {
        List<Transaction> transactionsByAccountNumber =
            transactionService.getAllTransactionsByAccountNumber(accountNumber);
        return new ResponseEntity<>(transactionsByAccountNumber, HttpStatus.OK);
    }

    @GetMapping(value = "/{accountNumber}/transactions", params = {"dateTime"})
    public ResponseEntity<List<Transaction>> getAccountTransactionsByDateTime(
        @PathVariable("accountNumber")
        @Size(min = 20, max = 20) String accountNumber,
        // Pattern "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"
        @RequestParam(name = "dateTime")
        @PastOrPresent
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            ZonedDateTime dateTime
    ) {
        List<Transaction> accountTransactionsByDateTime =
            transactionService.getAccountTransactionsByDateTime(accountNumber, dateTime);
        return new ResponseEntity<>(accountTransactionsByDateTime, HttpStatus.OK);
    }

    @GetMapping(value = "/{accountNumber}/transactions", params = {"date"})
    public ResponseEntity<List<Transaction>> getAccountTransactionsByDate(
        @PathVariable("accountNumber")
        @Size(min = 20, max = 20) String accountNumber,
        // Date pattern "yyyy-MM-dd"
        @RequestParam(name = "date")
        @PastOrPresent
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        List<Transaction> accountTransactionsByDate =
            transactionService.getAccountTransactionsByDate(accountNumber, date);
        return new ResponseEntity<>(accountTransactionsByDate, HttpStatus.OK);
    }

    @GetMapping(value = "/{accountNumber}/transactions", params = {"yearMonth"})
    public ResponseEntity<List<Transaction>> getAccountTransactionsByYearMonth(
        @PathVariable("accountNumber")
        @Size(min = 20, max = 20) String accountNumber,
        @RequestParam(name = "yearMonth")
        @PastOrPresent
        @DateTimeFormat(pattern = "yyyy-MM")
            YearMonth yearMonth
    ) {
        List<Transaction> accountTransactionsByYearMonth =
            transactionService.getAccountTransactionsByYearMonth(accountNumber, yearMonth);
        return new ResponseEntity<>(accountTransactionsByYearMonth, HttpStatus.OK);
    }

    @GetMapping(value = "/{accountNumber}/transactions", params = {"from", "to"})
    public ResponseEntity<List<Transaction>> getAccountTransactionsByPeriod(
        @PathVariable("accountNumber")
        @Size(min = 20, max = 20) String accountNumber,
        @RequestParam(name = "from")
        @PastOrPresent
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

        @RequestParam(name = "to")
        @PastOrPresent
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to
    ) {
        List<Transaction> accountTransactionsByPeriod =
            transactionService.getAccountTransactionsByPeriod(accountNumber, from, to);
        return new ResponseEntity<>(accountTransactionsByPeriod, HttpStatus.OK);
    }

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
