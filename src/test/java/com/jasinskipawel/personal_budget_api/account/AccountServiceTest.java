package com.jasinskipawel.personal_budget_api.account;

import com.jasinskipawel.personal_budget_api.account.exception.AccountAlreadyExistsException;
import com.jasinskipawel.personal_budget_api.account.exception.AccountHasTransactionsException;
import com.jasinskipawel.personal_budget_api.account.exception.AccountNotFoundException;
import com.jasinskipawel.personal_budget_api.transaction.Transaction;
import com.jasinskipawel.personal_budget_api.transaction.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void shouldCreateAccount_WhenNameIsUnique() {

        Account newAccount = new Account("Konto Oszczędnościowe");
        when(accountRepository.existsByName("Konto Oszczędnościowe")).thenReturn(false);
        when(accountRepository.save(newAccount)).thenReturn(newAccount);

        Account savedAccount = accountService.createAccount(newAccount);

        assertNotNull(savedAccount);
        assertEquals("Konto Oszczędnościowe", savedAccount.getName());
        verify(accountRepository).save(newAccount);
    }

    @Test
    void shouldThrowException_WhenCreatingAccountWithExistingName() {

        Account existingAccount = new Account("Konto Główne");
        when(accountRepository.existsByName("Konto Główne")).thenReturn(true);

        assertThrows(AccountAlreadyExistsException.class, () -> {
            accountService.createAccount(existingAccount);
        });

        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void shouldThrowException_WhenDeletingAccountWithTransactions() {

        Long accountId = 1L;
        Account account = new Account("Konto z transakcjami");
        account.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(transactionRepository.findByAccountId(accountId)).thenReturn(List.of(new Transaction()));

        assertThrows(AccountHasTransactionsException.class, () -> {
            accountService.deleteAccount(accountId);
        });

        verify(accountRepository, never()).delete(any(Account.class));
    }

    @Test
    void shouldDeleteAccount_WhenNoTransactions() {

        Long accountId = 1L;
        Account account = new Account("Konto do usunięcia");
        account.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        when(transactionRepository.findByAccountId(accountId)).thenReturn(List.of());

        accountService.deleteAccount(accountId);

        verify(accountRepository).delete(account);
    }

    @Test
    void shouldReturnAllAccounts() {

        Account acc1 = new Account("Konto 1");
        Account acc2 = new Account("Konto 2");
        when(accountRepository.findAll()).thenReturn(List.of(acc1, acc2));

        List<Account> accounts = accountService.getAllAccounts();

        assertEquals(2, accounts.size());
        verify(accountRepository).findAll();
    }

    @Test
    void shouldReturnAccount_WhenAccountExists() {

        Long accountId = 1L;
        Account account = new Account("Moje Konto");
        account.setId(accountId);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        Account foundAccount = accountService.getAccountById(accountId);

        assertNotNull(foundAccount);
        assertEquals("Moje Konto", foundAccount.getName());
        assertEquals(accountId, foundAccount.getId());
    }

    @Test
    void shouldThrowException_WhenGettingNonExistentAccount() {

        Long accountId = 999L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> {
            accountService.getAccountById(accountId);
        });

        verify(accountRepository).findById(accountId);
    }
}