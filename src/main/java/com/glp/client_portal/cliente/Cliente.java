package com.glp.client_portal.cliente;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "cliente")
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "Nome obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "Email obrigatório")
    @Email(message = "Email deve ser válido")
    @Column(nullable = false, unique = true)
    private String email;


    private String telefone;

    @NotBlank(message = "Documento é obrigatório")
    @Column(nullable = false, unique = true)
    private String documento; // CPF OU CNPJ

    @NotNull(message = "Tipo de documento é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento; // CPF OU CNPJ

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataCadastro;
}
