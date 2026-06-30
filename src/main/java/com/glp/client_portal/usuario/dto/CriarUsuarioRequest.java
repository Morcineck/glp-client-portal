package com.glp.client_portal.usuario.dto;

import com.glp.client_portal.usuario.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CriarUsuarioRequest (

        @Email
        @NotBlank
        String email,

        @NotBlank
        String senha,

        @NotNull
        Role role,

        UUID clienteId

){
}
