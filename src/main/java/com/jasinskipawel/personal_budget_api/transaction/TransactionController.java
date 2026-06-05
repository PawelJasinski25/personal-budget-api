package com.jasinskipawel.personal_budget_api.transaction;

import com.jasinskipawel.personal_budget_api.exception.ErrorResponse;
import com.jasinskipawel.personal_budget_api.transaction.dto.TransactionCreateRequest;
import com.jasinskipawel.personal_budget_api.transaction.dto.TransactionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@Tag(name = "Transakcje", description = "Zarządzanie przychodami i wydatkami")
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(summary = "Dodaj transakcję", description = "Tworzy transakcję (przychód/wydatek) i automatycznie aktualizuje saldo przypisanego konta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Transakcja dodana pomyślnie"),
            @ApiResponse(responseCode = "400", description = "Błędny format danych wejściowych", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Konto o podanym ID nie zostało znalezione", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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

    @Operation(summary = "Pobierz listę transakcji", description = "Zwraca wszystkie transakcje. Możliwe filtrowanie po kategorii oraz zakresie dat.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista transakcji pobrana pomyślnie"),
            @ApiResponse(responseCode = "400", description = "Błędny format danych wejściowych", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
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

    @Operation(summary = "Usuń transakcję", description = "Usuwa transakcję i cofa jej efekt na saldzie przypisanego konta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Transakcja usunięta pomyślnie"),
            @ApiResponse(responseCode = "404", description = "Transakcja o podanym ID nie istnieje", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

}
