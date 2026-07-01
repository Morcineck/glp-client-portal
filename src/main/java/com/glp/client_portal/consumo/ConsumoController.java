package com.glp.client_portal.consumo;

import com.glp.client_portal.cliente.Cliente;
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
@RequestMapping("/clientes/{clienteId}/contratos/{contratoId}/consumos")
public class ConsumoController {

    @Autowired
    private ConsumoService consumoService;

    @Autowired
    private AccessValidator accessValidator;

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
