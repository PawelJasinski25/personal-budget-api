package com.jasinskipawel.personal_budget_api.transaction;

import com.jasinskipawel.personal_budget_api.account.Account;
import com.jasinskipawel.personal_budget_api.account.exception.AccountNotFoundException;
import com.jasinskipawel.personal_budget_api.account.AccountRepository;
import com.jasinskipawel.personal_budget_api.transaction.dto.TransactionCreateRequest;
import com.jasinskipawel.personal_budget_api.transaction.exception.TransactionNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CsvTransactionExporter csvExporter;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository, CsvTransactionExporter csvTransactionExporter){
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.csvExporter = csvTransactionExporter;
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
            account.increaseBalance(transaction.getAmount());
        } else {
            account.decreaseBalance(transaction.getAmount());
        }

        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions(String category, LocalDate from, LocalDate to) {

        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("Data początkowa ('from') nie może być późniejsza niż data końcowa ('to').");
        }

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
            account.decreaseBalance(transaction.getAmount());
        } else {
            account.increaseBalance(transaction.getAmount());
        }

        accountRepository.save(account);
        transactionRepository.delete(transaction);
    }

    public byte[] exportTransactionsToCsv(Long accountId) {
        accountRepository.findById(accountId)
                .orElseThrow(() -> new AccountNotFoundException("Konto o ID " + accountId + " nie istnieje."));

        List<Transaction> transactions = transactionRepository.findByAccountId(accountId);
        return csvExporter.exportToCsv(transactions);
    }
}
