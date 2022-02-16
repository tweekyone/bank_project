package com.epam.bank.operatorinterface.controller.dto.response;

import com.epam.bank.operatorinterface.enumerated.TransactionOperationType;
import com.epam.bank.operatorinterface.enumerated.TransactionState;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TransactionImportResponse {

    @CsvBindByName(column = "Source Account", required = true)
    private String sourceAccount;

    @CsvBindByName(column = "Destination Account", required = true)
    private String destinationAccount;

    @CsvBindByName(required = true)
    private double amount;

    // example 2022-01-14T11:00:00
    @CsvDate(value = "yyyy-MM-dd'T'HH:mm:ss")
    @CsvBindByName(column = "Date Time", required = true)
    private LocalDateTime dateTime;

    @CsvBindByName(column = "Operation Type", required = true)
    private TransactionOperationType operationType;

    @CsvBindByName(required = true)
    private TransactionState state;
}
