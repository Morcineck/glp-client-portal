package com.glp.client_portal.consumo;

import com.glp.client_portal.consumo.dto.RegistrarConsumoRequest;
import com.glp.client_portal.contrato.Contrato;
import com.glp.client_portal.contrato.ContratoRepository;
import com.glp.client_portal.exception.IllegalArgumentBusinessException;
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
class ConsumoServiceTest {

    @Mock
    private ConsumoRepository consumoRepository;

    @Mock
    private ContratoRepository contratoRepository;

    @InjectMocks
    private ConsumoService consumoService;

    @Test
    void deveRegistrarConsumoNoContrato() {
        UUID contratoId = UUID.randomUUID();
        Contrato contrato = new Contrato();
        contrato.setId(contratoId);
        RegistrarConsumoRequest request = new RegistrarConsumoRequest(
                YearMonth.of(2026, 9), new BigDecimal("750.50"),
                new BigDecimal("620.30")
        );

        when(contratoRepository.findById(contratoId)).thenReturn(Optional.of(contrato));
        when(consumoRepository.save(any(ConsumoMensal.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ConsumoMensal consumo = consumoService.registrarConsumo(contratoId, request);

        assertSame(contrato, consumo.getContrato());
        assertEquals(request.mesReferencia(), consumo.getMesReferencia());
        assertEquals(request.kwhConsumido(), consumo.getKwhConsumido());
        assertEquals(request.custoTotal(), consumo.getCustoTotal());
    }

    @Test
    void naoDeveRegistrarConsumoParaContratoInexistente() {
        UUID contratoId = UUID.randomUUID();
        RegistrarConsumoRequest request = new RegistrarConsumoRequest(
                YearMonth.of(2026, 9), BigDecimal.ONE, BigDecimal.ONE
        );
        when(contratoRepository.findById(contratoId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> consumoService.registrarConsumo(contratoId, request));
        verifyNoInteractions(consumoRepository);
    }

    @Test
    void naoDeveRegistrarConsumoDuplicadoNaMesmaCompetencia() {
        UUID contratoId = UUID.randomUUID();
        Contrato contrato = new Contrato();
        contrato.setId(contratoId);
        RegistrarConsumoRequest request = new RegistrarConsumoRequest(
                YearMonth.of(2026, 9),
                new BigDecimal("750.50"),
                new BigDecimal("620.30")
        );

        when(contratoRepository.findById(contratoId)).thenReturn(Optional.of(contrato));
        when(consumoRepository.existsByContratoIdAndMesReferencia(
                contratoId,
                request.mesReferencia()
        )).thenReturn(true);

        assertThrows(
                IllegalArgumentBusinessException.class,
                () -> consumoService.registrarConsumo(contratoId, request)
        );
        verify(consumoRepository, never()).save(any(ConsumoMensal.class));
    }

    @Test
    void deveListarConsumosDeContratoExistente() {
        UUID contratoId = UUID.randomUUID();
        ConsumoMensal consumo = new ConsumoMensal();

        when(contratoRepository.existsById(contratoId)).thenReturn(true);
        when(consumoRepository.findByContratoId(contratoId)).thenReturn(List.of(consumo));

        assertEquals(1, consumoService.listarPorContrato(contratoId).size());
    }
}
