package com.glp.client_portal.usuario;


import com.glp.client_portal.usuario.dto.AlterarSenhaRequest;
import com.glp.client_portal.usuario.dto.CriarUsuarioRequest;
import com.glp.client_portal.usuario.dto.UsuarioResumoResponse;
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

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Gerenciamento de usuários do sistema")

public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;


    @Operation(summary = "Cadastrar usuário", description = "Cria um novo usuário. Acesso restrito a ADMIN.")
    @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "403", description = "Acesso negado")
    @PostMapping
    public ResponseEntity<Usuario> cadastrar(
            @Valid @RequestBody CriarUsuarioRequest request) {
        Usuario novoUsuario = usuarioService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }


    @Operation(summary = "Listar usuários", description = "Lista usuários e vínculos de acesso. Restrito a ADMIN.")
    @GetMapping
    public ResponseEntity<List<UsuarioResumoResponse>> listar() {
        return ResponseEntity.ok(usuarioService.listarResumos());
    }

    @Operation(summary = "Alterar senha")
    @ApiResponse(responseCode = "204", description = "Senha alterada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "401", description = "Token inválido ou não informado")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    @PatchMapping("/alterar_senha")
    public ResponseEntity<Void> alterarSenha(
            @Valid @RequestBody AlterarSenhaRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        usuarioService.alterarSenha(userDetails.getUsername(), request);
        return ResponseEntity.noContent().build();
    }


}
