package com.glp.client_portal.economia;

import com.glp.client_portal.cliente.ClienteRepository;
import com.glp.client_portal.contrato.Contrato;
import com.glp.client_portal.contrato.ContratoRepository;
import com.glp.client_portal.economia.dto.CalcularEconomiaRequest;
import com.glp.client_portal.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EconomiaServiceTest {

    @Mock
    private EconomiaRepository economiaRepository;
    @Mock
    private ContratoRepository contratoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    @InjectMocks
    private EconomiaService economiaService;

    @Test
    void deveCalcularEconomiaPelaDiferencaDosCustos() {
        UUID contratoId = UUID.randomUUID();
        Contrato contrato = new Contrato();
        contrato.setId(contratoId);
        CalcularEconomiaRequest request = new CalcularEconomiaRequest(
                YearMonth.of(2026, 9), new BigDecimal("1000.00"),
                new BigDecimal("650.00")
        );

        when(contratoRepository.findById(contratoId)).thenReturn(Optional.of(contrato));
        when(economiaRepository.save(any(Economia.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Economia economia = economiaService.calcularEconomia(contratoId, request);

        assertEquals(new BigDecimal("350.00"), economia.getEconomiaGerada());
        assertSame(contrato, economia.getContrato());
    }

    @Test
    void deveSomarEconomiaDeTodosOsContratosDoCliente() {
        UUID clienteId = UUID.randomUUID();
        Economia primeira = new Economia();
        primeira.setEconomiaGerada(new BigDecimal("100.50"));
        Economia segunda = new Economia();
        segunda.setEconomiaGerada(new BigDecimal("249.50"));

        when(clienteRepository.existsById(clienteId)).thenReturn(true);
        when(economiaRepository.findByContratoClienteId(clienteId))
                .thenReturn(List.of(primeira, segunda));

        assertEquals(new BigDecimal("350.00"),
                economiaService.totalEconomizadoPorCliente(clienteId));
    }

    @Test
    void naoDeveCalcularEconomiaParaContratoInexistente() {
        UUID contratoId = UUID.randomUUID();
        CalcularEconomiaRequest request = new CalcularEconomiaRequest(
                YearMonth.of(2026, 9), BigDecimal.TEN, BigDecimal.ONE
        );
        when(contratoRepository.findById(contratoId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> economiaService.calcularEconomia(contratoId, request));
        verifyNoInteractions(economiaRepository);
    }

    @Test
    void naoDeveCalcularTotalParaClienteInexistente() {
        UUID clienteId = UUID.randomUUID();
        when(clienteRepository.existsById(clienteId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> economiaService.totalEconomizadoPorCliente(clienteId));
    }
}
