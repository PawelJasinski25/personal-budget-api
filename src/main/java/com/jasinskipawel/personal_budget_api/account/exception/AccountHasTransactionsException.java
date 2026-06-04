package com.jasinskipawel.personal_budget_api.account.exception;

public class AccountHasTransactionsException extends RuntimeException {
    public AccountHasTransactionsException(String message) {
        super(message);
    }
}
