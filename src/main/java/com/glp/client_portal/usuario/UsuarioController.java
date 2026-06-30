package com.glp.client_portal.usuario;


import com.glp.client_portal.usuario.dto.AlterarSenhaRequest;
import com.glp.client_portal.usuario.dto.CriarUsuarioRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(
            @Valid @RequestBody CriarUsuarioRequest request) {
        Usuario novoUsuario = usuarioService.cadastrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoUsuario);
    }

    @PatchMapping("/alterar_senha")
    public ResponseEntity<Void> alterarSenha(
            @Valid @RequestBody AlterarSenhaRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        usuarioService.alterarSenha(userDetails.getUsername(), request);
        return ResponseEntity.noContent().build();
    }


}
