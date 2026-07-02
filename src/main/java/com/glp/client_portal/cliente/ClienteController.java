package com.glp.client_portal.cliente;

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
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Gerenciamento de clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private AccessValidator accessValidator;


    @Operation(summary = "Cadastrar cliente", description = "Cadastra um novo cliente no sistema. Acesso restrito ao perfil ADMIN.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou não informado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")})
    @PostMapping
    public ResponseEntity<Cliente> cadastrar(@Valid @RequestBody Cliente cliente) {
        Cliente novoCliente = clienteService.salvar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

    @Operation(summary = "Listar clientes", description = "ADMIN vê todos. CLIENTE vê apenas o próprio.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos(
            @AuthenticationPrincipal UserDetails userDetails) {

        // Se for CLIENTE, retorna só os dados dele
        UUID clienteIdDoUsuario = accessValidator.getClienteIdUsuario(userDetails);

        if (clienteIdDoUsuario == null) {
            return ResponseEntity.ok(clienteService.listarTodos());
        }

        // É CLIENTE — retorna só o dele
        return ResponseEntity.ok(List.of(clienteService.buscarPorId(clienteIdDoUsuario)));
    }

    @Operation(summary = "Buscar cliente por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado"),
            @ApiResponse(responseCode = "401", description = "Token inválido ou não informado"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable UUID id,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        accessValidator.validarAcessoCliente(userDetails, id);
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @Operation(summary = "Atualizar cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@PathVariable UUID id,
                                             @Valid @RequestBody Cliente cliente) {
        return ResponseEntity.ok(clienteService.atualizar(id, cliente));
    }

    @Operation(summary = "Deletar cliente")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cliente removido"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable UUID id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

}
