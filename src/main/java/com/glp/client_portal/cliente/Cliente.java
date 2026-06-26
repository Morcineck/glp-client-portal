package com.glp.client_portal.cliente;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    private String telefone;

    @Column(nullable = false, unique = true)
    private String documento; // CPF OU CNPJ

    @Enumerated(EnumType.STRING)
    private TipoDocumento tipoDocumento; // CPF OU CNPJ

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime dataCadastro;
}
