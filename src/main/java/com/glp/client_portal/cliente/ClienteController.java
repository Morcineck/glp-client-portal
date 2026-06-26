package com.glp.client_portal.cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@RequestBody Cliente cliente){
        Cliente novoCliente = clienteService.salvar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos(){
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable UUID id){
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable UUID id, @RequestBody Cliente cliente){
        return ResponseEntity.ok(clienteService.atualizar(id, cliente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id){
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
