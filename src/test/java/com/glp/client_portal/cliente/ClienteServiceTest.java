package com.glp.client_portal.cliente;

import com.glp.client_portal.cliente.dto.AtualizarClienteRequest;
import com.glp.client_portal.cliente.dto.CriarClienteRequest;
import com.glp.client_portal.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void deveCadastrarClienteComDadosDaRequisicao() {
        CriarClienteRequest request = new CriarClienteRequest(
                "Empresa Teste", "contato@teste.com", "21999999999",
                "04252011000110", TipoDocumento.CNPJ
        );

        when(clienteRepository.save(any(Cliente.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Cliente cliente = clienteService.salvar(request);

        assertEquals(request.nome(), cliente.getNome());
        assertEquals(request.email(), cliente.getEmail());
        assertEquals(request.documento(), cliente.getDocumento());
        assertEquals(request.tipoDocumento(), cliente.getTipoDocumento());
        assertNotNull(cliente.getDataCadastro());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveRejeitarDocumentoFiscalInvalido() {
        CriarClienteRequest request = new CriarClienteRequest(
                "Empresa Teste",
                "contato@teste.com",
                "21999999999",
                "12345678000199",
                TipoDocumento.CNPJ
        );

        assertThrows(
                com.glp.client_portal.exception.IllegalArgumentBusinessException.class,
                () -> clienteService.salvar(request)
        );

        verify(clienteRepository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoQuandoClienteNaoExiste() {
        UUID id = UUID.randomUUID();
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> clienteService.buscarPorId(id));
    }

    @Test
    void deveAtualizarClienteExistente() {
        UUID id = UUID.randomUUID();
        Cliente cliente = new Cliente();
        cliente.setId(id);
        cliente.setNome("Nome Antigo");

        AtualizarClienteRequest request = new AtualizarClienteRequest(
                "Nome Novo", "novo@teste.com", "21888888888",
                "52998224725", TipoDocumento.CPF
        );

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente atualizado = clienteService.atualizar(id, request);

        assertEquals("Nome Novo", atualizado.getNome());
        assertEquals("novo@teste.com", atualizado.getEmail());
        assertEquals(TipoDocumento.CPF, atualizado.getTipoDocumento());
        verify(clienteRepository).save(cliente);
    }

    @Test
    void deveValidarExistenciaAntesDeExcluir() {
        UUID id = UUID.randomUUID();
        Cliente cliente = new Cliente();
        cliente.setId(id);

        when(clienteRepository.findById(id)).thenReturn(Optional.of(cliente));

        clienteService.deletar(id);

        verify(clienteRepository).deleteById(id);
    }
}
