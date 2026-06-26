package com.glp.client_portal.contrato;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes/{clienteId}/contratos")
public class ContratoController {

    @Autowired
    private ContratoService contratoService;

    @PostMapping
    public ResponseEntity<Contrato> criar(@PathVariable UUID clienteId, @RequestBody Contrato contrato) {
        Contrato novoContrato = contratoService.salvar(clienteId, contrato);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoContrato);
    }

    @GetMapping
    public ResponseEntity<List<Contrato>> listarPorCliente(@PathVariable UUID clienteId) {
        return ResponseEntity.ok(contratoService.listarPorCliente(clienteId));
    }

    @GetMapping("/{contratoId}")
    public ResponseEntity<Contrato> buscarPorId(@PathVariable UUID clienteId, @PathVariable UUID contratoId) {
        return ResponseEntity.ok(contratoService.buscarPorId(contratoId));
    }
}
