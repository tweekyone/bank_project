package com.epam.bank.operatorinterface.service.importpkg;

import com.epam.bank.operatorinterface.controller.dto.response.TransactionImportResponse;
import com.epam.bank.operatorinterface.entity.Transaction;
import com.epam.bank.operatorinterface.entity.TransactionAccountData;
import com.epam.bank.operatorinterface.exception.TransactionCsvImportException;
import com.epam.bank.operatorinterface.repository.AccountRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionImporterCsv {

    private final char columnSeparator = ';';
    private final AccountRepository accountRepository;

    public List<Transaction> importCsv(InputStream inputStream) throws IOException {
        try (BufferedReader br =
                 new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            HeaderColumnNameMappingStrategy<TransactionImportResponse> strategy
                = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(TransactionImportResponse.class);

            CsvToBean<TransactionImportResponse> csvToBean =
                new CsvToBeanBuilder<TransactionImportResponse>(br)
                    .withMappingStrategy(strategy)
                    .withSeparator(columnSeparator)
                    .withOrderedResults(true)
                    .withIgnoreLeadingWhiteSpace(true)
                    .withVerifier(new ImportedCsvTransactionsVerifier())
                    .withThrowExceptions(true)
                    .build();

            List<TransactionImportResponse> importedTransactions = csvToBean.parse();

            List<Transaction> transactionList = new ArrayList<>();
            for (TransactionImportResponse t : importedTransactions) {
                transactionList.add(transactionBuilder(t));
            }
            return transactionList;
        } catch (Exception e) {
            throw new TransactionCsvImportException(e.getLocalizedMessage());
        }
    }

    private Transaction transactionBuilder(TransactionImportResponse transactionImport) {
        TransactionAccountData sourceAccount
            = new TransactionAccountData(transactionImport.getSourceAccount(),
            !accountRepository.existsByNumber(transactionImport.getSourceAccount()));

        TransactionAccountData destinationAccount
            = new TransactionAccountData(transactionImport.getDestinationAccount(),
            !accountRepository.existsByNumber(transactionImport.getDestinationAccount()));

        ZonedDateTime dateTime =
            transactionImport.getDateTime().atZone(ZoneId.of("Europe/Moscow")).withFixedOffsetZone();

        return new Transaction(sourceAccount, destinationAccount,
            transactionImport.getAmount(), dateTime,
            transactionImport.getOperationType(),
            transactionImport.getState());
    }
}
