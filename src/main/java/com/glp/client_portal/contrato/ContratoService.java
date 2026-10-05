package com.glp.client_portal.contrato;

import com.glp.client_portal.cliente.Cliente;
import com.glp.client_portal.cliente.ClienteService;
import com.glp.client_portal.contrato.dto.CriarContratoRequest;
import com.glp.client_portal.exception.IllegalArgumentBusinessException;
import com.glp.client_portal.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ContratoService {

    @Autowired
    private ContratoRepository contratoRepository;
    @Autowired
    private ClienteService clienteService;

    public Contrato salvar(UUID clienteId, CriarContratoRequest request) {
        if (request.dataFim().isBefore(request.dataInicio())) {
            throw new IllegalArgumentBusinessException(
                    "A data final do contrato não pode ser anterior à data inicial"
            );
        }

        Cliente cliente = clienteService.buscarPorId(clienteId);
        Contrato contrato = new Contrato();
        contrato.setCliente(cliente);
        contrato.setTipoContrato(request.tipoContrato());
        contrato.setDataInicio(request.dataInicio());
        contrato.setDataFim(request.dataFim());
        contrato.setValorMensal(request.valorMensal());
        contrato.setConsumoAntesKwh(request.consumoAntesKwh());
        contrato.setConsumoAtualKwh(request.consumoAtualKwh());
        return contratoRepository.save(contrato);
    }

    public List<Contrato> listarPorCliente(UUID clienteId) {
        clienteService.buscarPorId(clienteId);
        return contratoRepository.findByClienteId(clienteId);
    }

    public Contrato buscarPorId(UUID id) {
        return contratoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Contrato com ID [" + id + "] não existe na base de dados."));
    }

}
