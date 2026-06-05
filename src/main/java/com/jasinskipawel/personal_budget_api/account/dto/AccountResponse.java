package com.jasinskipawel.personal_budget_api.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Szczegółowe dane konta")
public record AccountResponse(
        @Schema(description = "Unikalny identyfikator konta", example = "1")
        Long id,

        @Schema(description = "Nazwa konta", example = "Konto Oszczędnościowe")
        String name,

        @Schema(description = "Aktualne saldo konta", example = "3500.50")
        BigDecimal balance) {
}
