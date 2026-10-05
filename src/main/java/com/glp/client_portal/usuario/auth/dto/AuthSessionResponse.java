package com.glp.client_portal.usuario.auth.dto;

public record AuthSessionResponse(
        String email,
        String role
) {
}
