package com.glp.client_portal.economia.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.YearMonth;

public record CalcularEconomiaRequest(

        @NotNull(message = "O mês de referência é obrigatório")
        @JsonFormat(pattern = "MM-yyyy")
        YearMonth mesReferencia,

        @NotNull(message = "O custo antes do contrato é obrigatório")
        @PositiveOrZero(message = "O custo antes do contrato não pode ser negativo")
        BigDecimal custoAntes,

        @NotNull(message = "O custo depois do contrato é obrigatório")
        @PositiveOrZero(message = "O custo depois do contrato não pode ser negativo")
        BigDecimal custoDepois

) {
}
