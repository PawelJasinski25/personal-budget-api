package com.jasinskipawel.personal_budget_api.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Dane wymagane do utworzenia nowego konta")
public record AccountCreateRequest(
        @Schema(description = "Nazwa konta (musi być unikalna)", example = "Konto Oszczędnościowe")
        @NotBlank(message = "Nazwa konta nie może być pusta")
        String name
) {}
