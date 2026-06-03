package com.jasinskipawel.personal_budget_api.transaction;

import com.jasinskipawel.personal_budget_api.transaction.dto.TransactionCreateRequest;
import com.jasinskipawel.personal_budget_api.transaction.dto.TransactionResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionCreateRequest request) {
        Transaction savedTransaction = transactionService.createTransaction(request);

        TransactionResponse response = new TransactionResponse(
                savedTransaction.getId(),
                savedTransaction.getAmount(),
                savedTransaction.getType(),
                savedTransaction.getCategory(),
                savedTransaction.getDescription(),
                savedTransaction.getDate(),
                savedTransaction.getAccount().getId()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {

        List<TransactionResponse> responses = transactionService.getAllTransactions(category, from, to).stream()
                .map(t -> new TransactionResponse(t.getId(), t.getAmount(), t.getType(), t.getCategory(), t.getDescription(), t.getDate(), t.getAccount().getId()))
                .toList();

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

}
