package com.glp.client_portal.consumo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.YearMonth;

public record RegistrarConsumoRequest(

        @NotNull(message = "O mês de referência é obrigatório")
        @JsonFormat(pattern = "MM-yyyy")
        YearMonth mesReferencia,

        @NotNull(message = "O consumo em kWh é obrigatório")
        @PositiveOrZero(message = "O consumo em kWh não pode ser negativo")
        BigDecimal kwhConsumido,

        @NotNull(message = "O custo total é obrigatório")
        @PositiveOrZero(message = "O custo total não pode ser negativo")
        BigDecimal custoTotal

) {
}
