package com.glp.client_portal.economia;

import com.glp.client_portal.economia.dto.CalcularEconomiaRequest;
import com.glp.client_portal.usuario.auth.security.AccessValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@RestController
@Tag(name = "Economia", description = "Cálculo e consulta de economia gerada")
public class EconomiaController {

    @Autowired
    private EconomiaService economiaService;

    @Autowired
    private AccessValidator accessValidator;


    @Operation(summary = "Calcular economia", description = "Calcula e registra a economia referente ao contrato informado.")
    @ApiResponse(responseCode = "201", description = "Calculo gerado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Token inválido ou não informado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @PostMapping("/clientes/{clienteId}/contratos/{contratoId}/economias")
    public ResponseEntity<Economia> calcularEconomia(
            @PathVariable UUID clienteId,
            @PathVariable UUID contratoId,
            @Valid @RequestBody CalcularEconomiaRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        accessValidator.validarAcessoCliente(userDetails, clienteId);

        Economia navaEconomia = economiaService.calcularEconomia(contratoId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(navaEconomia);
    }

    @Operation(summary = "Listar economia", description = "ADMIN vê todos. CLIENTE vê apenas o próprio.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @GetMapping("/clientes/{clienteId}/contratos/{contratoId}/economias")
    public ResponseEntity<List<Economia>> listarEconomia(
            @PathVariable UUID clienteId,
            @PathVariable UUID contratoId,
            @AuthenticationPrincipal UserDetails userDetails) {
        accessValidator.validarAcessoCliente(userDetails, clienteId);
        return ResponseEntity.ok(economiaService.listarPorContrato(contratoId));
    }

    @Operation(summary = "Calcular economia total")
    @ApiResponse(responseCode = "200", description = "Calculo gerado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Token inválido ou não informado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    @GetMapping("/clientes/{clienteId}/total-economizado")
    public ResponseEntity<BigDecimal> totalEconomizado(
            @PathVariable UUID clienteId,
            @AuthenticationPrincipal UserDetails userDetails) {
        accessValidator.validarAcessoCliente(userDetails, clienteId);
        return ResponseEntity.ok(economiaService.totalEconomizadoPorCliente(clienteId));
    }
}
