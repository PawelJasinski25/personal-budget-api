package com.jasinskipawel.personal_budget_api.transaction;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CsvTransactionExporterTest {

    private final CsvTransactionExporter exporter = new CsvTransactionExporter();

    @Test
    void shouldExportTransactionsToCsv_WithProperEscapingAndBOM() {

        Transaction transaction = new Transaction(
                new BigDecimal("150.00"),
                TransactionType.EXPENSE,
                "Jedzenie",
                "Zakupy; Biedronka\n drogo",
                LocalDate.of(2024, 10, 25),
                null
        );

        byte[] resultBytes = exporter.exportToCsv(List.of(transaction));
        String resultString = new String(resultBytes, StandardCharsets.UTF_8);

        assertTrue(resultString.startsWith("\uFEFF"));
        assertTrue(resultString.contains("ID;Kwota;Typ;Kategoria;Opis;Data\n"));
        assertTrue(resultString.contains("\"Zakupy; Biedronka\n drogo\""));

        assertTrue(resultString.contains("null;150.00;EXPENSE;Jedzenie;"));
    }
}

