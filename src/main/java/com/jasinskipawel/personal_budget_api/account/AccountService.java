package com.jasinskipawel.personal_budget_api.account;

import com.jasinskipawel.personal_budget_api.account.exception.AccountAlreadyExistsException;
import com.jasinskipawel.personal_budget_api.account.exception.AccountHasTransactionsException;
import com.jasinskipawel.personal_budget_api.account.exception.AccountNotFoundException;
import com.jasinskipawel.personal_budget_api.transaction.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Konto o ID " + id + " nie istnieje."));
    }

    public Account createAccount(Account account) {
        if (accountRepository.existsByName(account.getName())) {
            throw new AccountAlreadyExistsException("Konto o podanej nazwie już istnieje!");
        }
        return accountRepository.save(account);
    }


    public void deleteAccount(Long id) {
        Account account = getAccountById(id);

        if (!transactionRepository.findByAccountId(id).isEmpty()) {
            throw new AccountHasTransactionsException("Nie można usunąć konta, ponieważ posiada powiązane transakcje.");
        }

        accountRepository.delete(account);
    }
}
