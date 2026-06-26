package com.glp.client_portal.cliente;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente salvar(Cliente cliente) {
        cliente.setDataCadastro(LocalDateTime.now());
        return clienteRepository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Cliente buscarPorId(UUID id) {
        return clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public Cliente atualizar(UUID id, Cliente clienteAtualizado) {
        Cliente cliente = buscarPorId(id);
        cliente.setNome(clienteAtualizado.getNome());
        cliente.setEmail(clienteAtualizado.getEmail());
        cliente.setTelefone(clienteAtualizado.getTelefone());
        cliente.setDocumento(clienteAtualizado.getDocumento());
        cliente.setTipoDocumento(clienteAtualizado.getTipoDocumento());
        return clienteRepository.save(cliente);
    }

    public void deletar(UUID id) {
        clienteRepository.deleteById(id);
    }
}
