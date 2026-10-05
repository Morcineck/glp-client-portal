package com.glp.client_portal.usuario.auth.security;

import com.glp.client_portal.usuario.auth.dto.LoginRequest;
import com.glp.client_portal.usuario.auth.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
@RequestMapping("/login")
@Tag(name = "Autenticação", description = "Login e geração de token JWT")
public class AuthController {

    public static final String AUTH_COOKIE = "glp_auth";

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final boolean cookieSecure;
    private final String cookieSameSite;

    public AuthController(
            AuthService authService,
            JwtUtil jwtUtil,
            @Value("${app.auth.cookie-secure:false}") boolean cookieSecure,
            @Value("${app.auth.cookie-same-site:Lax}") String cookieSameSite
    ) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
        this.cookieSecure = cookieSecure;
        this.cookieSameSite = cookieSameSite;
    }

    @Operation(summary = "Realizar login", description = "Autentica o usuário e inicia a sessão segura do portal.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Email ou senha inválidos")
    })
    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest.email(), loginRequest.senha());
        String role = jwtUtil.extrairRoleToken(token);

        ResponseCookie authCookie = ResponseCookie.from(AUTH_COOKIE, token)
                .httpOnly(true)
                .secure(cookieSecure)
                .sameSite(cookieSameSite)
                .path("/")
                .maxAge(Duration.ofHours(1))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authCookie.toString())
                .body(new LoginResponse(token, loginRequest.email(), role));
    }
}
