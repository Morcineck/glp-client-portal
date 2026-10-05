package com.glp.client_portal.financeiro.dto;

import java.time.LocalDate;

public record RegistrarPagamentoRequest(
        LocalDate dataPagamento
) {
}
