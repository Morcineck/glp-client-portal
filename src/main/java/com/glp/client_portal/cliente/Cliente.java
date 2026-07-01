package com.glp.client_portal.cliente;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.glp.client_portal.usuario.Usuario;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@JsonPropertyOrder({"id", "nome", "email", "telefone", "documento", "tipoDocumento", "dataCadastro"})
@Table(name = "cliente")
@Data
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O Nome do cliente é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email informado não é válido")
    @Column(nullable = false, unique = true)
    private String email;


    private String telefone;

    @NotBlank(message = "O CPF ou CNPJ é obrigatório")
    @Column(nullable = false, unique = true)
    private String documento;

    @NotNull(message = "Informe se o documento é CPF ou CNPJ")
    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento; // CPF OU CNPJ

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataCadastro;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente")
    @lombok.ToString.Exclude
    @lombok.EqualsAndHashCode.Exclude
    private List<Usuario> usuarios = new ArrayList<>();
}
