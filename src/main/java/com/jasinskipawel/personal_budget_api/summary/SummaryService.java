package com.jasinskipawel.personal_budget_api.summary;

import com.jasinskipawel.personal_budget_api.summary.dto.SummaryResponse;
import com.jasinskipawel.personal_budget_api.transaction.Transaction;
import com.jasinskipawel.personal_budget_api.transaction.TransactionService;
import com.jasinskipawel.personal_budget_api.transaction.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SummaryService {
    private final TransactionService transactionService;

    public SummaryService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public SummaryResponse getSummary(LocalDate from, LocalDate to) {

        if (from != null && to != null && from.isAfter(to)) {
            throw new IllegalArgumentException("Data początkowa ('from') nie może być późniejsza niż data końcowa ('to').");
        }

        List<Transaction> transactions = transactionService.getAllTransactions(null, from, to);


        BigDecimal totalIncome = transactions.stream()
                .filter(t -> t.getType() == TransactionType.INCOME)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpense = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = totalIncome.subtract(totalExpense);

        Map<String, BigDecimal> expensesByCategory = transactions.stream()
                .filter(t -> t.getType() == TransactionType.EXPENSE)
                .collect(Collectors.toMap(
                        Transaction::getCategory,
                        Transaction::getAmount,
                        BigDecimal::add
                ));

        return new SummaryResponse(totalIncome, totalExpense, balance, expensesByCategory);
    }
}
