package com.jasinskipawel.personal_budget_api.transaction.dto;

import com.jasinskipawel.personal_budget_api.transaction.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionResponse(
        Long id,
        BigDecimal amount,
        TransactionType type,
        String category,
        String description,
        LocalDate date,
        Long accountId
) {
}