package com.glp.client_portal.dashboard;

import com.glp.client_portal.dashboard.dto.DashboardResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @Operation(summary = "Retorna os indicadores do dashboard conforme o perfil autenticado")
    @GetMapping
    public ResponseEntity<DashboardResponse> buscarResumo(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(dashboardService.buscarResumo(userDetails));
    }
}
