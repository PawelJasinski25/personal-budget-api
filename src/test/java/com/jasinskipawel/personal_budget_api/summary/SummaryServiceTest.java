package com.jasinskipawel.personal_budget_api.summary;

import com.jasinskipawel.personal_budget_api.account.Account;
import com.jasinskipawel.personal_budget_api.summary.dto.SummaryResponse;
import com.jasinskipawel.personal_budget_api.transaction.Transaction;
import com.jasinskipawel.personal_budget_api.transaction.TransactionService;
import com.jasinskipawel.personal_budget_api.transaction.TransactionType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SummaryServiceTest {

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private SummaryService summaryService;

    @Test
    void shouldCalculateSummaryCorrectly_WhenTransactionsExist() {

        Account account = new Account("Konto Testowe");

        Transaction income = new Transaction(new BigDecimal("5000.00"), TransactionType.INCOME, "Praca", "Wypłata", LocalDate.now(), account);
        Transaction expenseFood1 = new Transaction(new BigDecimal("50.00"), TransactionType.EXPENSE, "Jedzenie", "Biedronka", LocalDate.now(), account);
        Transaction expenseFood2 = new Transaction(new BigDecimal("100.00"), TransactionType.EXPENSE, "Jedzenie", "Lidl", LocalDate.now(), account);
        Transaction expenseTransport = new Transaction(new BigDecimal("200.00"), TransactionType.EXPENSE, "Transport", "Paliwo", LocalDate.now(), account);


        when(transactionService.getAllTransactions(null, null, null))
                .thenReturn(List.of(income, expenseFood1, expenseFood2, expenseTransport));

        SummaryResponse summary = summaryService.getSummary(null, null);

        assertNotNull(summary);

        assertEquals(0, new BigDecimal("5000.00").compareTo(summary.totalIncome()));
        assertEquals(0, new BigDecimal("350.00").compareTo(summary.totalExpense()));

        assertEquals(0, new BigDecimal("4650.00").compareTo(summary.balance()));

        assertEquals(2, summary.expensesByCategory().size());
        assertEquals(0, new BigDecimal("150.00").compareTo(summary.expensesByCategory().get("Jedzenie")));
        assertEquals(0, new BigDecimal("200.00").compareTo(summary.expensesByCategory().get("Transport")));

        verify(transactionService).getAllTransactions(null, null, null);
    }

    @Test
    void shouldReturnZeros_WhenNoTransactionsExist() {

        when(transactionService.getAllTransactions(null, null, null)).thenReturn(List.of());

        SummaryResponse summary = summaryService.getSummary(null, null);

        assertNotNull(summary);
        assertEquals(0, BigDecimal.ZERO.compareTo(summary.totalIncome()));
        assertEquals(0, BigDecimal.ZERO.compareTo(summary.totalExpense()));
        assertEquals(0, BigDecimal.ZERO.compareTo(summary.balance()));
        assertTrue(summary.expensesByCategory().isEmpty());

        verify(transactionService).getAllTransactions(null, null, null);
    }

    @Test
    void shouldPassDateFiltersToTransactionService_WhenDatesAreProvided() {
        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate to = LocalDate.of(2023, 1, 31);
        Account account = new Account("Konto Testowe");

        Transaction income = new Transaction(new BigDecimal("1000.00"), TransactionType.INCOME, "Praca", "Premia", from, account);

        when(transactionService.getAllTransactions(null, from, to)).thenReturn(List.of(income));

        SummaryResponse summary = summaryService.getSummary(from, to);

        assertNotNull(summary);
        assertEquals(0, new BigDecimal("1000.00").compareTo(summary.totalIncome()));

        verify(transactionService).getAllTransactions(null, from, to);
    }

    @Test
    void shouldThrowException_WhenDatesAreInvalid() {

        LocalDate from = LocalDate.of(2026, 12, 31);
        LocalDate to = LocalDate.of(2026, 1, 1);

        assertThrows(IllegalArgumentException.class, () -> {
            summaryService.getSummary(from, to);
        });

        verify(transactionService, never()).getAllTransactions(any(), any(), any());
    }
}