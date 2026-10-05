package com.glp.client_portal.usuario.auth.dto;

public record LoginResponse(
        String token,
        String email,
        String role
) {
}
