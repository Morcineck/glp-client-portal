package com.glp.client_portal.contrato;

import com.glp.client_portal.cliente.Cliente;
import com.glp.client_portal.cliente.ClienteService;
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

    public Contrato salvar(UUID clienteId, Contrato contrato) {
        Cliente cliente = clienteService.buscarPorId(clienteId);
        contrato.setCliente(cliente);
        return contratoRepository.save(contrato);
    }

    public List<Contrato> listarPorCliente(UUID clienteId) {
        return contratoRepository.findByClienteId(clienteId);
    }

    public Contrato buscarPorId(UUID id) {
        return contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato não encontrado!"));
    }

}
