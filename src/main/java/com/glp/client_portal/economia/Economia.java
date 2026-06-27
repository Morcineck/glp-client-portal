package com.glp.client_portal.economia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.glp.client_portal.contrato.Contrato;
import com.glp.client_portal.converter.YearMonthConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

@Entity
@JsonPropertyOrder({"id", "contrato", "mesReferencia", "custoAntes", "custoDepois", "economiaGerada"})
@Table(name = "economia")
@Data
public class Economia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;


    @JsonFormat(pattern = "MM-yyyy")
    @Convert(converter = YearMonthConverter.class)
    @NotNull(message = "O mês de referência é obrigatório")
    private YearMonth mesReferencia;
    @NotNull(message = "O custo antes do contrato é obrigatório")
    private BigDecimal custoAntes;
    @NotNull(message = "O custo depois do contrato é obrigatório")
    private BigDecimal custoDepois;
    private BigDecimal economiaGerada;


}
