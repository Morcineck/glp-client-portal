package com.glp.client_portal.usuario;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.glp.client_portal.cliente.Cliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.UUID;


@JsonPropertyOrder({"id", "email", "role", "cliente"})
@Entity
@Table(name = "usuario")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email informado não é válido")
    @Column(nullable = false, unique = true)
    private String email;


    @NotBlank(message = "A senha é obrigatória")
    @JsonIgnore
    @Column(nullable = false)
    private String senha;

    @NotNull(message = "O perfil de acesso (role) é obrigatório")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Cliente cliente;

}
