package com.jasinskipawel.personal_budget_api.transaction.dto;

import com.jasinskipawel.personal_budget_api.transaction.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Dane wymagane do dodania nowej transakcji")
public record TransactionCreateRequest(

        @Schema(description = "Opcjonalny opis transakcji", example = "Zakupy na weekend")
        String description,

        @Schema(description = "Kwota transakcji (musi być większa od 0)", example = "150.50")
        @NotNull(message = "Kwota jest wymagana")
        @Positive(message = "Kwota musi być większa od zera")
        BigDecimal amount,

        @Schema(description = "Typ transakcji (INCOME - przychód, EXPENSE - wydatek)", example = "EXPENSE")
        @NotNull(message = "Typ transakcji jest wymagany ")
        TransactionType type,

        @Schema(description = "Kategoria transakcji", example = "Jedzenie")
        @NotBlank(message = "Kategoria jest wymagana")
        String category,

        @Schema(description = "Data transakcji (jeśli nie podano, użyta zostanie dzisiejsza)", example = "2024-03-15")
        LocalDate date,

        @Schema(description = "ID konta, z którym powiązana jest transakcja", example = "1")
        @NotNull(message = "ID konta jest wymagane")
        Long accountId
) {

}
