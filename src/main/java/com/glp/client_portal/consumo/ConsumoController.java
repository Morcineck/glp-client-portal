package com.glp.client_portal.consumo;

import com.glp.client_portal.usuario.auth.security.AccessValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/clientes/{clienteId}/contratos/{contratoId}/consumos")
@Tag(name = "Consumo Mensal", description = "Registro e consulta de consumo mensal")
public class ConsumoController {

    @Autowired
    private ConsumoService consumoService;

    @Autowired
    private AccessValidator accessValidator;

    @Operation(summary = "Registar consumo", description = "Registra o consumo mensal de cada cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consumo registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")})
    @PostMapping
    public ResponseEntity<ConsumoMensal> registrarConsumo(
            @PathVariable UUID clienteId,
            @PathVariable UUID contratoId,
            @Valid @RequestBody ConsumoMensal consumo,
            @AuthenticationPrincipal UserDetails userDetails) {

        accessValidator.validarAcessoCliente(userDetails, clienteId);

        ConsumoMensal novoConsumo = consumoService.registrarConsumo(contratoId, consumo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoConsumo);
    }

    @Operation(summary = "Listar consumos", description = "Retorna o histórico de consumo mensal do contrato informado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou não informado"),
            @ApiResponse(responseCode = "404", description = "Contrato não encontrado")})
    @GetMapping
    public ResponseEntity<List<ConsumoMensal>> listarPorContrato(
            @PathVariable UUID clienteId,
            @PathVariable UUID contratoId,
            @AuthenticationPrincipal UserDetails userDetails) {
        accessValidator.validarAcessoCliente(userDetails, clienteId);
        List<ConsumoMensal> historico = consumoService.listarPorContrato(contratoId);
        return ResponseEntity.ok(historico);
    }

}
