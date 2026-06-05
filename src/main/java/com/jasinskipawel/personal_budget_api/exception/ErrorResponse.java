package com.jasinskipawel.personal_budget_api.exception;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Obiekt zwracany w przypadku błędu API")
public record ErrorResponse(
        @Schema(description = "Opis błędu", example = "Szczegółowy komunikat błędu z serwera")
        String error) {
}
