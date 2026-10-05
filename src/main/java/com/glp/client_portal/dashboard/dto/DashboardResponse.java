package com.glp.client_portal.dashboard.dto;

import java.math.BigDecimal;

public record DashboardResponse(
        long totalClientes,
        long totalContratos,
        long totalUsuarios,
        BigDecimal consumoTotalKwh,
        BigDecimal economiaTotal
) {
}
