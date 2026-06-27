package com.glp.client_portal.consumo;

import com.glp.client_portal.contrato.Contrato;
import com.glp.client_portal.contrato.ContratoRepository;
import com.glp.client_portal.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ConsumoService {

    @Autowired
    private ConsumoRepository consumoRepository;

    @Autowired
    private ContratoRepository contratoRepository;

    public ConsumoMensal registrarConsumo(UUID contratoId, ConsumoMensal consumo) {
        Contrato contrato = contratoRepository.findById(contratoId)
                .orElseThrow(() -> new ResourceNotFoundException("Contrato com o ID [" + contratoId + "] não foi encontrado"));

        consumo.setContrato(contrato);
        return consumoRepository.save(consumo);

    }

    public List<ConsumoMensal> listarPorContrato(UUID contratoId) {
        if (!contratoRepository.existsById(contratoId)) {
            throw new ResourceNotFoundException("Contrato com o ID [" + contratoId + "] não foi encontrado");
        }
        return consumoRepository.findByContratoId(contratoId);

    }

}
