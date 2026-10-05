package com.glp.client_portal.financeiro.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

public record CriarMensalidadeRequest(
        @NotNull(message = "O mês de referência é obrigatório")
        YearMonth mesReferencia,

        @NotNull(message = "A data de vencimento é obrigatória")
        LocalDate vencimento,

        @Positive(message = "O valor deve ser maior que zero")
        BigDecimal valor
) {
}
