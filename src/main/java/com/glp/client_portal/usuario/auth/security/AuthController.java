package com.glp.client_portal.usuario.auth.security;


import com.glp.client_portal.usuario.auth.dto.LoginRequest;
import com.glp.client_portal.usuario.auth.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Tag(name = "Autenticação", description = "Login e geração de token JWT")
public class AuthController {

    @Autowired
    private AuthService authService;



    @Operation(summary = "Realizar login", description = "Autentica o usuário com email e senha e retorna um token JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Email ou senha inválidos")
    })
    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest.email(),  loginRequest.senha());
        return ResponseEntity.ok(new LoginResponse(token));

    }
}
