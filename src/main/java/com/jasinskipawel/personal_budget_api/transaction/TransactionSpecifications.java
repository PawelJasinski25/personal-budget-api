package com.jasinskipawel.personal_budget_api.transaction;

import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class TransactionSpecifications {

    public static Specification<Transaction> hasCategory(String category) {
        return (root, query, criteriaBuilder) -> {
            if (category == null || category.isBlank()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("category"), category);
        };
    }

    public static Specification<Transaction> hasDateFrom(LocalDate from) {
        return (root, query, criteriaBuilder) -> {
            if (from == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThanOrEqualTo(root.get("date"), from);
        };
    }

    public static Specification<Transaction> hasDateTo(LocalDate to) {
        return (root, query, criteriaBuilder) -> {
            if (to == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get("date"), to);
        };
    }
}
