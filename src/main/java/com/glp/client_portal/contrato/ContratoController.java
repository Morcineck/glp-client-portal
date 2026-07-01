package com.glp.client_portal.contrato;

import com.glp.client_portal.usuario.auth.security.AccessValidator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes/{clienteId}/contratos")
public class ContratoController {

    @Autowired
    private ContratoService contratoService;

    @Autowired
    private AccessValidator accessValidator;

    @PostMapping
    public ResponseEntity<Contrato> criar(
            @PathVariable UUID clienteId,
            @Valid @RequestBody Contrato contrato,
            @AuthenticationPrincipal UserDetails userDetails) {
        accessValidator.validarAcessoCliente(userDetails, clienteId);
        Contrato novoContrato = contratoService.salvar(clienteId, contrato);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoContrato);
    }

    @GetMapping
    public ResponseEntity<List<Contrato>> listarPorCliente(
            @PathVariable UUID clienteId,
            @AuthenticationPrincipal UserDetails userDetails) {
        accessValidator.validarAcessoCliente(userDetails, clienteId);
        return ResponseEntity.ok(contratoService.listarPorCliente(clienteId));
    }

    @GetMapping("/{contratoId}")
    public ResponseEntity<Contrato> buscarPorId(
            @PathVariable UUID clienteId,
            @PathVariable UUID contratoId,
            @AuthenticationPrincipal UserDetails userDetails) {
        accessValidator.validarAcessoCliente(userDetails, clienteId);
        return ResponseEntity.ok(contratoService.buscarPorId(contratoId));
    }
}
