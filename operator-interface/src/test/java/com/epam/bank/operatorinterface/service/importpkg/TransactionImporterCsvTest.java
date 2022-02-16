package com.epam.bank.operatorinterface.service.importpkg;

import com.epam.bank.operatorinterface.entity.Transaction;
import com.epam.bank.operatorinterface.repository.AccountRepository;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TransactionImporterCsvTest {

    private static final String FILES_DIR = "src/test/java/com/epam/bank/operatorinterface/service/importpkg/csv/";

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionImporterCsv importerCsv;

    @Test
    void shouldReturnTransactionListFromCsv() throws IOException {
        final var inputStream = new FileInputStream(FILES_DIR + "lost_transactions_extended.csv");
        final var transactions = importerCsv.importCsv(inputStream);

        Assertions.assertThat(transactions.size()).isEqualTo(3);
        Assertions.assertThat(transactions).hasOnlyElementsOfType(Transaction.class);
    }

    @Test
    void shouldThrowRuntimeExceptionIfFileHasWrongSeparator() throws FileNotFoundException {
        final var inputStream = new FileInputStream(FILES_DIR + "transactions_with_wrong_separator.csv");
        final var exception = Assertions.catchThrowable(
            () -> importerCsv.importCsv(inputStream)
        );
        Assertions.assertThat(exception)
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void shouldThrowRuntimeExceptionIfFileHasWrongFields() throws IOException {
        final var inputStream = new FileInputStream(FILES_DIR + "transactions_with_wrong_fields.csv");

        final var exception = Assertions.catchThrowable(
            () -> importerCsv.importCsv(inputStream)
        );
        Assertions.assertThat(exception)
            .isInstanceOf(RuntimeException.class);
    }

    @Test
    void shouldThrowFileNotFoundExceptionIfFileNotFound() {
        Assertions.assertThatThrownBy(
            () -> importerCsv
            .importCsv(new FileInputStream(RandomStringUtils.randomAlphanumeric(100))))
            .isInstanceOf(FileNotFoundException.class);
    }
}