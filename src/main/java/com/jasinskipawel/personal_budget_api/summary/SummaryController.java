package com.jasinskipawel.personal_budget_api.summary;

import com.jasinskipawel.personal_budget_api.exception.ErrorResponse;
import com.jasinskipawel.personal_budget_api.summary.dto.SummaryResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Tag(name = "Podsumowania", description = "Generowanie raportów finansowych budżetu osobistego")
@RequestMapping(value="/api/summary", produces = "application/json")
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService) {
        this.summaryService = summaryService;
    }

    @Operation(summary = "Pobierz podsumowanie finansowe", description = "Zwraca łączne przychody, wydatki, saldo oraz wydatki pogrupowane po kategorii. Opcjonalnie filtrowane po datach.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Podsumowanie wygenerowano pomyślnie"),
            @ApiResponse(responseCode = "400", description = "Błędny format danych wejściowych", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<SummaryResponse> getSummary(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to
    ) {
        return ResponseEntity.ok(summaryService.getSummary(from, to));
    }
}
