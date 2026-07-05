package com.glp.client_portal.consumo.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.YearMonth;

public record RegistrarConsumoRequest(

        @NotNull(message = "O mês de referência é obrigatório")
        YearMonth mesReferencia,

        @NotNull(message = "O consumo em kWh é obrigatório")
        BigDecimal kwhConsumido,

        @NotNull(message = "O custo total é obrigatório")
        BigDecimal custoTotal


) {
}
