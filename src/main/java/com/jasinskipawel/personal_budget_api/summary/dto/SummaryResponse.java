package com.jasinskipawel.personal_budget_api.summary.dto;

import java.math.BigDecimal;
import java.util.Map;

public record SummaryResponse(
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal balance,
        Map<String, BigDecimal> expensesByCategory
) {
}
