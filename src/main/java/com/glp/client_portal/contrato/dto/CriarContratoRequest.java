package com.glp.client_portal.contrato.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CriarContratoRequest(

        @NotBlank(message = "O tipo de contrato é obrigatório")
        String tipoContrato,

        @NotNull(message = "A data de início dp contrato é obrigatória")
        LocalDate dataInicio,

        @NotNull(message = "A data de fim do contrato é obrigatória")
        LocalDate dataFim,

        @NotNull(message = "O valor mensal do contrato é obrigatório")
        BigDecimal valorMensal,

        @NotNull(message = "O consumo anterior é obrigatório")
        BigDecimal consumoAntesKwh,

        @NotNull(message = "O consumo atual é obrigatório")
        BigDecimal consumoAtualKwh

) {}
