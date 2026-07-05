package com.glp.client_portal.economia.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.YearMonth;

public record CalcularEconomiaRequest(

        @NotNull(message = "O mês de referência é obrigatório")
        YearMonth mesReferencia,

        @NotNull(message = "O custo antes do contrato é obrigatório")
        BigDecimal custoAntes,

        @NotNull(message = "O custo depois do contrato é obrigatório")
        BigDecimal custoDepois


) {
}
