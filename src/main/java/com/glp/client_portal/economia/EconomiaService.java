package com.glp.client_portal.economia;

import com.glp.client_portal.cliente.ClienteRepository;
import com.glp.client_portal.contrato.Contrato;
import com.glp.client_portal.contrato.ContratoRepository;
import com.glp.client_portal.exception.ResourceNotFoundException;
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

    @Autowired
    private ClienteRepository clienteRepository;


    public Economia calcularEconomia(UUID contratoId, Economia economia) {
        Contrato contrato = contratoRepository.findById(contratoId)
                .orElseThrow(() -> new ResourceNotFoundException("Contrato não encontrado"));
        economia.setContrato(contrato);
        economia.setEconomiaGerada(economia.getCustoAntes().subtract(economia.getCustoDepois()));

        return economiaRepository.save(economia);
    }

    public List<Economia> listarPorContrato(UUID contratoId) {
        if(!contratoRepository.existsById(contratoId)) {
            throw new ResourceNotFoundException("Contrato não encontrado com esse id");
        }
        return economiaRepository.findByContratoId(contratoId);
    }

    public BigDecimal totalEconomizadoPorCliente(UUID clienteId) {
        if(!clienteRepository.existsById(clienteId)) {
            throw new ResourceNotFoundException("Cliente com ID [" + clienteId + "] não encontrado.");
        }

        List<Economia> economias = economiaRepository.findByContratoClienteId(clienteId);

        return economias.stream()
                .map(Economia::getEconomiaGerada)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
