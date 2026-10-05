package com.glp.client_portal.contrato.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CriarContratoRequest(

        @NotBlank(message = "O tipo de contrato é obrigatório")
        String tipoContrato,

        @NotNull(message = "A data de início do contrato é obrigatória")
        LocalDate dataInicio,

        @NotNull(message = "A data de fim do contrato é obrigatória")
        LocalDate dataFim,

        @NotNull(message = "O valor mensal do contrato é obrigatório")
        @Positive(message = "O valor mensal deve ser maior que zero")
        BigDecimal valorMensal,

        @NotNull(message = "O consumo anterior é obrigatório")
        @PositiveOrZero(message = "O consumo anterior não pode ser negativo")
        BigDecimal consumoAntesKwh,

        @NotNull(message = "O consumo atual é obrigatório")
        @PositiveOrZero(message = "O consumo atual não pode ser negativo")
        BigDecimal consumoAtualKwh

) {
}
