package com.jasinskipawel.personal_budget_api.account.dto;

import java.math.BigDecimal;

public record AccountResponse(Long id, String name, BigDecimal balance) {
}
