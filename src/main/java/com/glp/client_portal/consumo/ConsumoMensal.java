package com.glp.client_portal.consumo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.glp.client_portal.contrato.Contrato;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.UUID;

@Entity
@Table(name = "consumo_mensal")
@Data
public class ConsumoMensal {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;

    @JsonFormat(pattern = "MM-yyyy")
    private YearMonth mesReferencia;
    private BigDecimal kwhConsumido;
    private BigDecimal custoTotal;
}
