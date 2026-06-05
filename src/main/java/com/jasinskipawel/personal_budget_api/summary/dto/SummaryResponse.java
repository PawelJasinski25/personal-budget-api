package com.jasinskipawel.personal_budget_api.summary.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Map;

@Schema(description = "Podsumowanie finansowe budżetu")
public record SummaryResponse(
        @Schema(description = "Suma wszystkich przychodów", example = "5000.00")
        BigDecimal totalIncome,

        @Schema(description = "Suma wszystkich wydatków", example = "1200.50")
        BigDecimal totalExpense,

        @Schema(description = "Całkowity bilans (przychody minus wydatki)", example = "3799.50")
        BigDecimal balance,

        @Schema(description = "Zestawienie wydatków pogrupowane według kategorii", example = "{\"Jedzenie\": 800.00, \"Transport\": 400.50}")
        Map<String, BigDecimal> expensesByCategory
) {
}
