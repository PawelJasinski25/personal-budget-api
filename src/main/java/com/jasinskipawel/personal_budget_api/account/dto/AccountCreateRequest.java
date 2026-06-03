package com.jasinskipawel.personal_budget_api.account.dto;

import jakarta.validation.constraints.NotBlank;

public record AccountCreateRequest(
        @NotBlank(message = "Nazwa konta nie może być pusta")
        String name
) {}
