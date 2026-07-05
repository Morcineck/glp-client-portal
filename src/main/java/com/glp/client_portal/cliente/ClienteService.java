package com.glp.client_portal.cliente;

import com.glp.client_portal.cliente.dto.AtualizarClienteRequest;
import com.glp.client_portal.cliente.dto.CriarClienteRequest;
import com.glp.client_portal.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente salvar(CriarClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNome(request.nome());
        cliente.setEmail(request.email());
        cliente.setTelefone(request.telefone());
        cliente.setDocumento(request.documento());
        cliente.setTipoDocumento(request.tipoDocumento());
        cliente.setDataCadastro(LocalDateTime.now());
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(UUID id) {
        return clienteRepository.findById(id).orElseThrow(()
                -> new ResourceNotFoundException("Cliente não encontrado"));
    }

    public Cliente atualizar(UUID id, AtualizarClienteRequest request) {
        Cliente cliente = buscarPorId(id);
        cliente.setNome(request.nome());
        cliente.setEmail(request.email());
        cliente.setTelefone(request.telefone());
        cliente.setDocumento(request.documento());
        cliente.setTipoDocumento(request.tipoDocumento());
        return clienteRepository.save(cliente);
    }

    public void deletar(UUID id) {
        buscarPorId(id); // valida que o cliente existe antes de deletar
        clienteRepository.deleteById(id);
    }
}
