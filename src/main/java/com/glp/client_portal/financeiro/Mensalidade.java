package com.glp.client_portal.financeiro;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.glp.client_portal.contrato.Contrato;
import com.glp.client_portal.converter.YearMonthConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;

@Entity
@Table(name = "mensalidade", uniqueConstraints = @UniqueConstraint(\n        name = "uk_mensalidade_contrato_mes",\n        columnNames = {"contrato_id", "mes_referencia"}\n))
@Data
public class Mensalidade {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    @JsonFormat(pattern = "MM-yyyy")
    @Convert(converter = YearMonthConverter.class)
    @NotNull
    private YearMonth mesReferencia;

    @NotNull
    @Positive
    private BigDecimal valor;

    @JsonFormat(pattern = "dd-MM-yyyy")
    @NotNull
    private LocalDate vencimento;

    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate dataPagamento;

    @Enumerated(EnumType.STRING)
    @NotNull
    private MensalidadeStatus status;
}
