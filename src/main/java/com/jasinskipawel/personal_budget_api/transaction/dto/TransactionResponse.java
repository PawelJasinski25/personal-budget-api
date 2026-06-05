package com.jasinskipawel.personal_budget_api.transaction.dto;

import com.jasinskipawel.personal_budget_api.transaction.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Szczegółowe dane transakcji")
public record TransactionResponse(
        @Schema(description = "Unikalny identyfikator transakcji", example = "10")
        Long id,

        @Schema(description = "Kwota transakcji", example = "150.50")
        BigDecimal amount,

        @Schema(description = "Typ transakcji", example = "EXPENSE")
        TransactionType type,

        @Schema(description = "Kategoria", example = "Jedzenie")
        String category,

        @Schema(description = "Opis transakcji", example = "Zakupy na weekend")
        String description,

        @Schema(description = "Data wykonania transakcji", example = "2024-03-15")
        LocalDate date,

        @Schema(description = "ID konta powiązanego z transakcją", example = "1")
        Long accountId
) {
}