package com.glp.client_portal.usuario.dto;

import com.glp.client_portal.usuario.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CriarUsuarioRequest (

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres")
        String senha,

        @NotNull
        Role role,

        UUID clienteId

){
}
