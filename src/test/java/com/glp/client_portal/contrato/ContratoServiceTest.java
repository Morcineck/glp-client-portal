package com.glp.client_portal.contrato;

import com.glp.client_portal.cliente.Cliente;
import com.glp.client_portal.cliente.ClienteService;
import com.glp.client_portal.contrato.dto.CriarContratoRequest;
import com.glp.client_portal.exception.IllegalArgumentBusinessException;
import com.glp.client_portal.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContratoServiceTest {

    @Mock
    private ContratoRepository contratoRepository;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ContratoService contratoService;

    @Test
    void deveCadastrarContratoVinculadoAoCliente() {
        UUID clienteId = UUID.randomUUID();
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        CriarContratoRequest request = new CriarContratoRequest(
                "ENERGIA", LocalDate.of(2026, 1, 1), LocalDate.of(2027, 1, 1),
                new BigDecimal("500.00"), new BigDecimal("1000.00"),
                new BigDecimal("800.00")
        );

        when(clienteService.buscarPorId(clienteId)).thenReturn(cliente);
        when(contratoRepository.save(any(Contrato.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Contrato contrato = contratoService.salvar(clienteId, request);

        assertSame(cliente, contrato.getCliente());
        assertEquals(request.tipoContrato(), contrato.getTipoContrato());
        assertEquals(request.valorMensal(), contrato.getValorMensal());
        verify(contratoRepository).save(contrato);
    }

    @Test
    void deveValidarClienteAntesDeListarContratos() {
        UUID clienteId = UUID.randomUUID();
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        Contrato contrato = new Contrato();

        when(clienteService.buscarPorId(clienteId)).thenReturn(cliente);
        when(contratoRepository.findByClienteId(clienteId)).thenReturn(List.of(contrato));

        List<Contrato> contratos = contratoService.listarPorCliente(clienteId);

        assertEquals(1, contratos.size());
        verify(clienteService).buscarPorId(clienteId);
    }

    @Test
    void deveRejeitarContratoComDataFinalAnteriorAInicial() {
        UUID clienteId = UUID.randomUUID();
        CriarContratoRequest request = new CriarContratoRequest(
                "ENERGIA",
                LocalDate.of(2027, 1, 1),
                LocalDate.of(2026, 12, 31),
                new BigDecimal("500.00"),
                new BigDecimal("1000.00"),
                new BigDecimal("800.00")
        );

        IllegalArgumentBusinessException exception = assertThrows(
                IllegalArgumentBusinessException.class,
                () -> contratoService.salvar(clienteId, request)
        );

        assertEquals(
                "A data final do contrato não pode ser anterior à data inicial",
                exception.getMessage()
        );
        verifyNoInteractions(clienteService, contratoRepository);
    }

    @Test
    void deveLancarExcecaoQuandoContratoNaoExiste() {
        UUID contratoId = UUID.randomUUID();
        when(contratoRepository.findById(contratoId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> contratoService.buscarPorId(contratoId));
    }
}
