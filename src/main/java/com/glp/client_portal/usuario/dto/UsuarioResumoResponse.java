package com.glp.client_portal.usuario.dto;

import com.glp.client_portal.usuario.Role;

import java.util.UUID;

public record UsuarioResumoResponse(
        UUID id,
        String email,
        Role role,
        UUID clienteId
) {
}
