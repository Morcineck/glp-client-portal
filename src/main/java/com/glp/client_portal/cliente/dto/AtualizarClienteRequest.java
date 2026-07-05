package com.glp.client_portal.cliente.dto;

import com.glp.client_portal.cliente.TipoDocumento;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizarClienteRequest(

        @NotBlank(message = "O nome do cliente é obrigatório")
        String nome,

        @NotBlank(message = "O email é obrigatório")
        @Email(message = "o email informado não é válido")
        String email,

        String telefone,

        @NotBlank(message = "O CPF ou CPNJ é obrigatório")
        String documento,

        @NotNull(message = "Informe se o documento é CPF ou CPNJ")
        TipoDocumento tipoDocumento


) {
}
