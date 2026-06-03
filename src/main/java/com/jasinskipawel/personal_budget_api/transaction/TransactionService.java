package com.jasinskipawel.personal_budget_api.transaction;

import com.jasinskipawel.personal_budget_api.account.Account;
import com.jasinskipawel.personal_budget_api.account.AccountNotFoundException;
import com.jasinskipawel.personal_budget_api.account.AccountRepository;
import com.jasinskipawel.personal_budget_api.transaction.dto.TransactionCreateRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository){
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public Transaction createTransaction(TransactionCreateRequest request) {

        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new AccountNotFoundException("Konto o ID " + request.accountId() + " nie istnieje."));

        LocalDate transactionDate;
        if (request.date() == null) {
            transactionDate = LocalDate.now();
        }
        else{
            transactionDate = request.date();
        }

        Transaction transaction = new Transaction(
                request.amount(),
                request.type(),
                request.category(),
                request.description(),
                transactionDate,
                account
        );

        if (request.type() == TransactionType.INCOME) {
            account.setBalance(account.getBalance().add(request.amount()));
        } else {
            account.setBalance(account.getBalance().subtract(request.amount()));
        }

        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions(String category, LocalDate from, LocalDate to) {

        Specification<Transaction> spec = Specification.where(TransactionSpecifications.hasCategory(category))
                .and(TransactionSpecifications.hasDateFrom(from))
                .and(TransactionSpecifications.hasDateTo(to));

        return transactionRepository.findAll(spec);
    }

    @Transactional
    public void deleteTransaction(Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transakcja o ID " + id + " nie istnieje."));

        Account account = transaction.getAccount();


        if (transaction.getType() == TransactionType.INCOME) {
            account.setBalance(account.getBalance().subtract(transaction.getAmount()));
        } else {
            account.setBalance(account.getBalance().add(transaction.getAmount()));
        }

        accountRepository.save(account);
        transactionRepository.delete(transaction);
    }
}
