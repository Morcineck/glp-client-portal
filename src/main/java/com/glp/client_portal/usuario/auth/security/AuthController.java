package com.glp.client_portal.usuario.auth.security;


import com.glp.client_portal.usuario.auth.dto.LoginRequest;
import com.glp.client_portal.usuario.auth.dto.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest.email(),  loginRequest.senha());
        return ResponseEntity.ok(new LoginResponse(token));

    }




}
