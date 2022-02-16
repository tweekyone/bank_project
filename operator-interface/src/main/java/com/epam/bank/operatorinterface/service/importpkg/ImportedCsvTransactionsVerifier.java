package com.epam.bank.operatorinterface.service.importpkg;

import com.epam.bank.operatorinterface.controller.dto.response.TransactionImportResponse;
import com.opencsv.bean.BeanVerifier;
import com.opencsv.exceptions.CsvConstraintViolationException;

public class ImportedCsvTransactionsVerifier implements BeanVerifier<TransactionImportResponse> {

    @Override
    public boolean verifyBean(TransactionImportResponse bean) throws CsvConstraintViolationException {
        if (bean.getSourceAccount().equalsIgnoreCase("null")) {
            throw new CsvConstraintViolationException(bean, "Incorrect source account");
        }
        if (bean.getDestinationAccount().equalsIgnoreCase("null")) {
            throw new CsvConstraintViolationException(bean, "Incorrect destination account");
        }

        return true;
    }
}
