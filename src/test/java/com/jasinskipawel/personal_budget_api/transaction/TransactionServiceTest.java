package com.jasinskipawel.personal_budget_api.transaction;

import com.jasinskipawel.personal_budget_api.account.Account;
import com.jasinskipawel.personal_budget_api.account.AccountRepository;
import com.jasinskipawel.personal_budget_api.account.exception.AccountNotFoundException;
import com.jasinskipawel.personal_budget_api.transaction.dto.TransactionCreateRequest;
import com.jasinskipawel.personal_budget_api.transaction.exception.TransactionNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void shouldCreateIncomeTransaction_AndIncreaseAccountBalance() {
        Long accountId = 1L;
        Account account = new Account("Konto Główne");
        account.setId(accountId);
        account.increaseBalance(new BigDecimal("100.00"));

        TransactionCreateRequest request = new TransactionCreateRequest(
                "Wypłata pensji",
                new BigDecimal("5000.00"),
                TransactionType.INCOME,
                "Praca",
                LocalDate.now(),
                accountId
        );

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction savedTransaction = transactionService.createTransaction(request);

        assertNotNull(savedTransaction);
        assertEquals(TransactionType.INCOME, savedTransaction.getType());
        assertEquals(0, new BigDecimal("5100.00").compareTo(account.getBalance()));

        verify(accountRepository).save(account);
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void shouldCreateExpenseTransaction_AndDecreaseAccountBalance() {
        Long accountId = 1L;
        Account account = new Account("Konto Główne");
        account.setId(accountId);
        account.increaseBalance(new BigDecimal("100.00"));

        TransactionCreateRequest request = new TransactionCreateRequest(
                "Zakupy",
                new BigDecimal("30.00"),
                TransactionType.EXPENSE,
                "Jedzenie",
                LocalDate.now(),
                accountId
        );

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction savedTransaction = transactionService.createTransaction(request);

        assertNotNull(savedTransaction);
        assertEquals(TransactionType.EXPENSE, savedTransaction.getType());
        assertEquals(0, new BigDecimal("70.00").compareTo(account.getBalance()));

        verify(accountRepository).save(account);
    }

    @Test
    void shouldThrowException_WhenCreatingTransactionForNonExistentAccount() {
        Long nonExistentAccountId = 999L;

        TransactionCreateRequest request = new TransactionCreateRequest(
                "Opis",
                new BigDecimal("500.00"),
                TransactionType.INCOME,
                "Premia",
                LocalDate.now(),
                nonExistentAccountId
        );

        when(accountRepository.findById(nonExistentAccountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            transactionService.createTransaction(request);
        });

        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void shouldDeleteIncomeTransaction_AndDecreaseAccountBalance() {
        Long transactionId = 1L;
        Account account = new Account("Konto Główne");
        account.increaseBalance(new BigDecimal("200.00"));

        Transaction transaction = new Transaction(
                new BigDecimal("50.00"),
                TransactionType.INCOME,
                "Praca",
                "Wypłata",
                LocalDate.now(),
                account
        );

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(transactionId);

        assertEquals(0, new BigDecimal("150.00").compareTo(account.getBalance()));

        verify(accountRepository).save(account);
        verify(transactionRepository).delete(transaction);
    }

    @Test
    void shouldDeleteExpenseTransaction_AndIncreaseAccountBalance() {
        Long transactionId = 1L;
        Account account = new Account("Konto Główne");
        account.increaseBalance(new BigDecimal("200.00"));

        Transaction transaction = new Transaction(
                new BigDecimal("50.00"),
                TransactionType.EXPENSE,
                "Jedzenie",
                "Zwrot za zakupy",
                LocalDate.now(),
                account
        );

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));

        transactionService.deleteTransaction(transactionId);

        assertEquals(0, new BigDecimal("250.00").compareTo(account.getBalance()));

        verify(accountRepository).save(account);
        verify(transactionRepository).delete(transaction);
    }


    @Test
    void shouldThrowException_WhenDeletingNonExistentTransaction() {
        Long nonExistentTransactionId = 999L;

        when(transactionRepository.findById(nonExistentTransactionId)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> {
            transactionService.deleteTransaction(nonExistentTransactionId);
        });

        verify(accountRepository, never()).save(any(Account.class));
        verify(transactionRepository, never()).delete(any(Transaction.class));
    }

    @Test
    void shouldReturnTransactionsFilteredByCategory() {
        String category = "Jedzenie";
        Transaction t1 = new Transaction(new BigDecimal("50.00"), TransactionType.EXPENSE, category, "Opis", LocalDate.now(), new Account());

        when(transactionRepository.findAll(Mockito.<Specification<Transaction>>any())).thenReturn(List.of(t1));

        List<Transaction> result = transactionService.getAllTransactions(category, null, null);

        assertEquals(1, result.size());
        assertEquals(category, result.getFirst().getCategory());
        verify(transactionRepository).findAll(Mockito.<Specification<Transaction>>any());
    }

    @Test
    void shouldReturnTransactionsFilteredByDateRange() {
        LocalDate from = LocalDate.of(2023, 1, 1);
        LocalDate to = LocalDate.of(2023, 1, 31);
        Transaction t1 = new Transaction(new BigDecimal("50.00"), TransactionType.EXPENSE, "Inne", "Opis", LocalDate.of(2023, 1, 15), new Account());

        when(transactionRepository.findAll(Mockito.<Specification<Transaction>>any())).thenReturn(List.of(t1));

        List<Transaction> result = transactionService.getAllTransactions(null, from, to);

        assertEquals(1, result.size());
        verify(transactionRepository).findAll(Mockito.<Specification<Transaction>>any());
    }

    @Test
    void shouldThrowException_WhenGettingTransactionsWithInvalidDates() {
        LocalDate from = LocalDate.of(2026, 12, 31);
        LocalDate to = LocalDate.of(2026, 1, 1);

        assertThrows(IllegalArgumentException.class, () -> {
            transactionService.getAllTransactions(null, from, to);
        });
    }
}
