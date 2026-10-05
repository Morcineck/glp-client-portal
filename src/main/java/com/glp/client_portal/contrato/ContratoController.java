package com.glp.client_portal.contrato;

import com.glp.client_portal.contrato.dto.CriarContratoRequest;
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

import java.util.List;
import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/clientes/{clienteId}/contratos")
@Tag(name = "Contratos", description = "Gerenciamento de contratos de energia")
public class ContratoController {

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private AccessValidator accessValidator;


    @Operation(summary = "Cadastrar contrato", description = "Cria um novo contrato. Acesso restrito a ADMIN.")
    @ApiResponse(responseCode = "201", description = "Contrato registrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Token inválido ou não informado")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @PostMapping
    public ResponseEntity<Contrato> criar(
            @PathVariable UUID clienteId,
            @Valid @RequestBody CriarContratoRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        accessValidator.validarAcessoCliente(userDetails, clienteId);
        Contrato novoContrato = contratoService.salvar(clienteId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoContrato);
    }

    @Operation(summary = "Listar contratos", description = "ADMIN vê todos. CLIENTE vê apenas o próprio.")
    @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "401", description = "Token inválido ou não informado")
    @ApiResponse(responseCode = "404", description = "Contrato não encontrado")
    @GetMapping
    public ResponseEntity<List<Contrato>> listarPorCliente(
            @PathVariable UUID clienteId,
            @AuthenticationPrincipal UserDetails userDetails) {
        accessValidator.validarAcessoCliente(userDetails, clienteId);
        return ResponseEntity.ok(contratoService.listarPorCliente(clienteId));
    }

    @Operation(summary = "Buscar contratos por ID")
    @ApiResponse(responseCode = "200", description = "Contrato encontrado ")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @ApiResponse(responseCode = "401", description = "Token inválido ou não informado")
    @ApiResponse(responseCode = "404", description = "Contrato não encontrado")
    @GetMapping("/{contratoId}")
    public ResponseEntity<Contrato> buscarPorId(
            @PathVariable UUID clienteId,
            @PathVariable UUID contratoId,
            @AuthenticationPrincipal UserDetails userDetails) {
        accessValidator.validarAcessoCliente(userDetails, clienteId);
        accessValidator.validarContratoDoCliente(clienteId, contratoId);
        return ResponseEntity.ok(contratoService.buscarPorId(contratoId));
    }
}
