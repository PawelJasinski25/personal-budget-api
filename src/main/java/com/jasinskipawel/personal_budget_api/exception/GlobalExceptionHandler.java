package com.jasinskipawel.personal_budget_api.exception;

import com.jasinskipawel.personal_budget_api.account.AccountAlreadyExistsException;
import com.jasinskipawel.personal_budget_api.account.AccountNotFoundException;
import com.jasinskipawel.personal_budget_api.transaction.TransactionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAccountAlreadyExists(AccountAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleAccountNotFound(AccountNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransactionNotFound(TransactionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonErrors(HttpMessageNotReadableException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Przekazano nieprawidłowy format danych (np. zły typ transakcji lub błędny format daty)."));
    }
}
