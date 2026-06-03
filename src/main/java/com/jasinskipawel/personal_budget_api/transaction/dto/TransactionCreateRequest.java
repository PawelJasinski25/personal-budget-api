package com.jasinskipawel.personal_budget_api.transaction.dto;

import com.jasinskipawel.personal_budget_api.transaction.TransactionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionCreateRequest(

        String description,

        @NotNull(message = "Kwota jest wymagana")
        @Positive(message = "Kwota musi być większa od zera")
        BigDecimal amount,

        @NotNull(message = "Typ transakcji jest wymagany ")
        TransactionType type,

        @NotBlank(message = "Kategoria jest wymagana")
        String category,

        LocalDate date,

        @NotNull(message = "ID konta jest wymagane")
        Long accountId
) {

}
