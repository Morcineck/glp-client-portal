package com.glp.client_portal.economia;

import com.glp.client_portal.contrato.Contrato;
import com.glp.client_portal.contrato.ContratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class EconomiaService {

    @Autowired
    private EconomiaRepository economiaRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    public Economia calcularEconomia(UUID contratoId, Economia economia) {
        Contrato contrato = contratoRepository.findById(contratoId)
                .orElseThrow(() -> new RuntimeException("Contrato não encontrado"));
        economia.setContrato(contrato);
        economia.setEconomiaGerada(economia.getCustoAntes().subtract(economia.getCustoDepois()));

        return economiaRepository.save(economia);
    }

    public List<Economia> listaPorContrato(UUID contratoId) {
        if(!contratoRepository.existsById(contratoId)) {
            throw new RuntimeException("Contrato não encontrado com esse id");
        }
        return economiaRepository.findByContratoId(contratoId);
    }

    public BigDecimal totalEconomizadoPorCliente(UUID clienteId) {
        List<Economia> economias = economiaRepository.findByContratoClienteId(clienteId);

        return economias.stream()
                .map(Economia::getEconomiaGerada)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
