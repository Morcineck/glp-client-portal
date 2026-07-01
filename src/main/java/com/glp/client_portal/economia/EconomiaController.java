package com.glp.client_portal.economia;

import com.glp.client_portal.usuario.auth.security.AccessValidator;
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

@RestController
public class EconomiaController {

    @Autowired
    private EconomiaService economiaService;

    @Autowired
    private AccessValidator accessValidator;


    @PostMapping("/clientes/{clienteId}/contratos/{contratoId}/economias")
    public ResponseEntity<Economia> calcularEconomia(
            @PathVariable UUID clienteId,
            @PathVariable UUID contratoId,
            @Valid @RequestBody Economia economia,
            @AuthenticationPrincipal UserDetails userDetails) {
        accessValidator.validarAcessoCliente(userDetails, clienteId);

        Economia navaEconomia = economiaService.calcularEconomia(contratoId, economia);
        return ResponseEntity.status(HttpStatus.CREATED).body(navaEconomia);
    }

    @GetMapping("/clientes/{clienteId}/contratos/{contratoId}/economias")
    public ResponseEntity<List<Economia>> listarEconomia(
            @PathVariable UUID clienteId,
            @PathVariable UUID contratoId,
            @AuthenticationPrincipal UserDetails userDetails) {
        accessValidator.validarAcessoCliente(userDetails, clienteId);
        return ResponseEntity.ok(economiaService.listarPorContrato(contratoId));
    }

    @GetMapping("/clientes/{clienteId}/total-economizado")
    public ResponseEntity<BigDecimal> totalEconomizado(
            @PathVariable UUID clienteId,
            @AuthenticationPrincipal UserDetails userDetails) {
        accessValidator.validarAcessoCliente(userDetails, clienteId);
        return ResponseEntity.ok(economiaService.totalEconomizadoPorCliente(clienteId));
    }
}
