package com.glp.client_portal.contrato;

import com.glp.client_portal.cliente.Cliente;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "contratos")
@Data
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    private String tipoContrato;
    private String dataInicio;
    private String dataFim;
    private BigDecimal valorMensal;
    private BigDecimal consumoAntesKwh;
    private BigDecimal consumoAtualKwh;

}
