package com.glp.client_portal.financeiro.dto;

import java.math.BigDecimal;

public record FinanceiroResumoResponse(
        BigDecimal receitaPrevista,
        BigDecimal receitaRecebida,
        BigDecimal valorEmAberto,
        BigDecimal valorVencido,
        long mensalidadesPagas,
        long mensalidadesPendentes,
        long mensalidadesVencidas
) {
}
