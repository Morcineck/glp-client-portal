package com.glp.client_portal.consumo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes/{clienteId}/contratos/{contratoId}/consumos")
public class ConsumoController {

    @Autowired
    private ConsumoService consumoService;

    @PostMapping
    public ResponseEntity<ConsumoMensal> registrarConsumo(
            @PathVariable UUID clienteId,
            @PathVariable UUID contratoId,
            @RequestBody ConsumoMensal consumo) {

        ConsumoMensal novoConsumo = consumoService.registrarConsumo(contratoId, consumo);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoConsumo);
    }

    @GetMapping
    public ResponseEntity<List<ConsumoMensal>> listarPorContrato(
            @PathVariable UUID clienteId,
            @PathVariable UUID contratoId) {
        List<ConsumoMensal> historico = consumoService.listarPorContrato(contratoId);
        return ResponseEntity.ok(historico);
    }

}
