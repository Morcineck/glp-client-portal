package com.glp.client_portal.consumo;

import com.glp.client_portal.consumo.dto.RegistrarConsumoRequest;
import com.glp.client_portal.contrato.Contrato;
import com.glp.client_portal.contrato.ContratoRepository;
import com.glp.client_portal.exception.IllegalArgumentBusinessException;
import com.glp.client_portal.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ConsumoService {

    @Autowired
    private ConsumoRepository consumoRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    public ConsumoMensal registrarConsumo(UUID contratoId, RegistrarConsumoRequest request) {
        Contrato contrato = contratoRepository.findById(contratoId)
                .orElseThrow(() -> new ResourceNotFoundException("Contrato não foi encontrado"));

        if (consumoRepository.existsByContratoIdAndMesReferencia(contratoId, request.mesReferencia())) {
            throw new IllegalArgumentBusinessException(
                    "Já existe um consumo registrado para este contrato na competência "
                            + request.mesReferencia()
            );
        }

        ConsumoMensal consumo = new ConsumoMensal();
        consumo.setContrato(contrato);
        consumo.setMesReferencia(request.mesReferencia());
        consumo.setKwhConsumido(request.kwhConsumido());
        consumo.setCustoTotal(request.custoTotal());

        return consumoRepository.save(consumo);
    }

    public List<ConsumoMensal> listarPorContrato(UUID contratoId) {
        if (!contratoRepository.existsById(contratoId)) {
            throw new ResourceNotFoundException(
                    "Contrato com o ID [" + contratoId + "] não foi encontrado"
            );
        }

        return consumoRepository.findByContratoId(contratoId).stream()
                .sorted(Comparator.comparing(ConsumoMensal::getMesReferencia))
                .toList();
    }
}
