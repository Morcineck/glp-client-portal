package com.glp.client_portal.usuario.dto;

import com.glp.client_portal.usuario.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record CriarUsuarioRequest(

        @Email(message = "O e-mail informado não é válido")
        @NotBlank(message = "O e-mail é obrigatório")
        String email,

        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
                message = "A senha deve conter letra maiúscula, letra minúscula e número"
        )
        String senha,

        @NotNull(message = "O perfil de acesso é obrigatório")
        Role role,

        UUID clienteId

) {
}
