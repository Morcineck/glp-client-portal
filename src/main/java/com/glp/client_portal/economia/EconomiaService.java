package com.glp.client_portal.economia;

import com.glp.client_portal.cliente.ClienteRepository;
import com.glp.client_portal.contrato.Contrato;
import com.glp.client_portal.contrato.ContratoRepository;
import com.glp.client_portal.economia.dto.CalcularEconomiaRequest;
import com.glp.client_portal.exception.IllegalArgumentBusinessException;
import com.glp.client_portal.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
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

    public Economia calcularEconomia(UUID contratoId, CalcularEconomiaRequest request) {
        Contrato contrato = contratoRepository.findById(contratoId)
                .orElseThrow(() -> new ResourceNotFoundException("Contrato não encontrado"));

        if (economiaRepository.existsByContratoIdAndMesReferencia(contratoId, request.mesReferencia())) {
            throw new IllegalArgumentBusinessException(
                    "Já existe uma economia registrada para este contrato na competência "
                            + request.mesReferencia()
            );
        }

        if (request.custoDepois().compareTo(request.custoAntes()) > 0) {
            throw new IllegalArgumentBusinessException(
                    "O custo atual não pode ser maior que o custo anterior para registrar economia"
            );
        }

        Economia economia = new Economia();
        economia.setContrato(contrato);
        economia.setMesReferencia(request.mesReferencia());
        economia.setCustoAntes(request.custoAntes());
        economia.setCustoDepois(request.custoDepois());
        economia.setEconomiaGerada(request.custoAntes().subtract(request.custoDepois()));

        return economiaRepository.save(economia);
    }

    public List<Economia> listarPorContrato(UUID contratoId) {
        if (!contratoRepository.existsById(contratoId)) {
            throw new ResourceNotFoundException("Contrato não encontrado com esse id");
        }

        return economiaRepository.findByContratoId(contratoId).stream()
                .sorted(Comparator.comparing(Economia::getMesReferencia))
                .toList();
    }

    public BigDecimal totalEconomizadoPorCliente(UUID clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new ResourceNotFoundException(
                    "Cliente com ID [" + clienteId + "] não encontrado."
            );
        }

        return economiaRepository.findByContratoClienteId(clienteId).stream()
                .map(Economia::getEconomiaGerada)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
