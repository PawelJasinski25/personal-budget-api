package com.jasinskipawel.personal_budget_api.transaction;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(String message) {
        super(message);
    }
}
