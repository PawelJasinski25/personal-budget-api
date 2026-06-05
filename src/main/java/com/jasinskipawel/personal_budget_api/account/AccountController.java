package com.jasinskipawel.personal_budget_api.account;

import com.jasinskipawel.personal_budget_api.account.dto.AccountCreateRequest;
import com.jasinskipawel.personal_budget_api.account.dto.AccountResponse;
import com.jasinskipawel.personal_budget_api.exception.ErrorResponse;
import com.jasinskipawel.personal_budget_api.transaction.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "Konta", description = "Zarządzanie kontami budżetu osobistego")
@RequestMapping(value = "/api/accounts", produces = "application/json")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @Operation(summary = "Pobierz wszystkie konta", description = "Zwraca listę wszystkich kont wraz z ich aktualnym saldem.")
    @ApiResponse(responseCode = "200", description = "Lista kont pobrana pomyślnie")
    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> responses = accountService.getAllAccounts().stream()
                .map(acc -> new AccountResponse(acc.getId(), acc.getName(), acc.getBalance()))
                .toList();

        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Pobierz szczegóły konta", description = "Zwraca dane konkretnego konta na podstawie jego ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Konto znalezione"),
            @ApiResponse(responseCode = "404", description = "Konto o podanym ID nie istnieje", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);

        AccountResponse response = new AccountResponse(
                account.getId(),
                account.getName(),
                account.getBalance()
        );

        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Utwórz nowe konto", description = "Tworzy nowe konto do śledzenia budżetu z unikalną nazwą.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Konto zostało pomyślnie utworzone"),
            @ApiResponse(responseCode = "400", description = "Błędny format danych wejściowych", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Konto o podanej nazwie już istnieje", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        Account accountToSave = new Account(request.name());
        Account savedAccount = accountService.createAccount(accountToSave);

        AccountResponse response = new AccountResponse(
                savedAccount.getId(),
                savedAccount.getName(),
                savedAccount.getBalance()
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Usuń konto", description = "Usuwa wskazane konto. Konto można usunąć tylko wtedy, gdy nie ma przypisanych żadnych transakcji.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Konto usunięte pomyślnie"),
            @ApiResponse(responseCode = "404", description = "Konto o podanym ID nie istnieje", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Konto posiada transakcje i nie może zostać usunięte", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eksport transakcji konta do pliku CSV",
            description = "Zwraca plik CSV ze wszystkimi transakcjami dla podanego konta.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plik CSV został pomyślnie wygenerowany",
                    content = @Content(mediaType = "text/csv")),
            @ApiResponse(responseCode = "400", description = "Błędny format ID konta (np. podano litery zamiast cyfr)",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Konto o podanym ID nie istnieje",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}/transactions/export", produces = "text/csv; charset=UTF-8")
    public ResponseEntity<byte[]> exportTransactionsToCsv(@PathVariable Long id) {

        byte[] csvContent = transactionService.exportTransactionsToCsv(id);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"transactions_account_" + id + ".csv\"");

        return ResponseEntity.ok()
                .headers(headers)
                .body(csvContent);
    }
}
