package com.jasinskipawel.personal_budget_api.account.exception;

public class AccountAlreadyExistsException extends RuntimeException{
    public AccountAlreadyExistsException(String message){
        super(message);
    }
}
