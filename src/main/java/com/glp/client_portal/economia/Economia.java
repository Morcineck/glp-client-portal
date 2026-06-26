package com.glp.client_portal.economia;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.glp.client_portal.consumo.ConsumoMensal;
import com.glp.client_portal.contrato.Contrato;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.UUID;

@Entity
@Table(name = "economia")
@Data
public class Economia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "contrato_id", nullable = false)
    private Contrato contrato;


    private YearMonth mesReferencia;

    private BigDecimal custoAntes;
    private BigDecimal custoDepois;
    private BigDecimal economiaGerada;


}
