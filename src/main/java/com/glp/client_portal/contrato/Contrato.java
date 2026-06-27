package com.glp.client_portal.contrato;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.glp.client_portal.cliente.Cliente;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@JsonPropertyOrder({"id", "cliente", "tipoContrato", "dataInicio", "dataFim", "valorMensal",
        "consumoAntesKwh", "consumoAtualKwh"})
@Table(name = "contrato")
@Data
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotBlank(message = "O tipo de contrato é obrigatório")
    private String tipoContrato;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "A data de início do contrato é obrigatória")
    private LocalDate dataInicio;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "A data de fim do contrato é obrigatória")
    private LocalDate dataFim;

    @NotNull(message = "O valor mensal do contrato é obrigatório")
    @Positive(message = "O valor mensal deve ser maior que zero")
    private BigDecimal valorMensal;

    @NotNull(message = "O consumo anterior em kWh é obrigatório")
    @PositiveOrZero(message = "O consumo anterior não pode ser negativo")
    private BigDecimal consumoAntesKwh;

    @NotNull(message = "O consumo atual em kWh é obrigatório")
    @PositiveOrZero(message = "O consumo atual não pode ser negativo")
    private BigDecimal consumoAtualKwh;

}
