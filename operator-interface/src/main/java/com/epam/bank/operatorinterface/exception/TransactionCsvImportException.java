package com.epam.bank.operatorinterface.exception;

import com.opencsv.bean.exceptionhandler.CsvExceptionHandler;
import com.opencsv.exceptions.CsvException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public class TransactionCsvImportException extends RuntimeException implements CsvExceptionHandler {
    public TransactionCsvImportException() {
        super("Import of file is corrupted");
    }

    public TransactionCsvImportException(MultipartFile file) {
        super(String.format("Import of file: %s is corrupted", file.getName()));
    }

    public TransactionCsvImportException(String localizedMessage) {
        super(localizedMessage);
    }

    public TransactionCsvImportException(List<CsvException> capturedExceptions) {
        super(capturedExceptions.toString());
    }

    @Override
    public CsvException handleException(CsvException e) throws CsvException {
        return new CsvException(e.getLocalizedMessage());
    }
}
