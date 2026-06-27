package com.glp.client_portal.consumo;

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
@JsonPropertyOrder({"id", "contrato", "mesReferencia", "kwhConsumido", "custoTotal"})
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
    @Convert(converter = YearMonthConverter.class)
    @NotNull(message = "O mês de referência é obrigatório")
    private YearMonth mesReferencia;
    @NotNull(message = "O consumo em kWh é obrigatório")
    private BigDecimal kwhConsumido;
    @NotNull(message = "O custo total é obrigatório")
    private BigDecimal custoTotal;
}
