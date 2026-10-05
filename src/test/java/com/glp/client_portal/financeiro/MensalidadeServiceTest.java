package com.glp.client_portal.financeiro;

import com.glp.client_portal.contrato.Contrato;
import com.glp.client_portal.contrato.ContratoRepository;
import com.glp.client_portal.exception.IllegalArgumentBusinessException;
import com.glp.client_portal.financeiro.dto.CriarMensalidadeRequest;
import com.glp.client_portal.financeiro.dto.FinanceiroResumoResponse;
import com.glp.client_portal.financeiro.dto.RegistrarPagamentoRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MensalidadeServiceTest {

    @Mock
    private MensalidadeRepository mensalidadeRepository;

    @Mock
    private ContratoRepository contratoRepository;

    @InjectMocks
    private MensalidadeService mensalidadeService;

    @Test
    void deveCriarMensalidadeComValorDoContratoQuandoValorNaoInformado() {
        UUID contratoId = UUID.randomUUID();
        Contrato contrato = new Contrato();
        contrato.setId(contratoId);
        contrato.setValorMensal(new BigDecimal("1500.00"));

        CriarMensalidadeRequest request = new CriarMensalidadeRequest(
                YearMonth.of(2026, 10),
                LocalDate.now().plusDays(10),
                null
        );

        when(contratoRepository.findById(contratoId)).thenReturn(Optional.of(contrato));
        when(mensalidadeRepository.save(any(Mensalidade.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Mensalidade mensalidade = mensalidadeService.criar(contratoId, request);

        assertEquals(new BigDecimal("1500.00"), mensalidade.getValor());
        assertEquals(MensalidadeStatus.PENDENTE, mensalidade.getStatus());
        assertSame(contrato, mensalidade.getContrato());
    }

    @Test
    void naoDeveCriarMensalidadeDuplicadaNaMesmaCompetencia() {
        UUID contratoId = UUID.randomUUID();
        Contrato contrato = new Contrato();
        contrato.setId(contratoId);
        contrato.setValorMensal(new BigDecimal("1500.00"));

        CriarMensalidadeRequest request = new CriarMensalidadeRequest(
                YearMonth.of(2026, 10),
                LocalDate.now().plusDays(10),
                null
        );

        when(contratoRepository.findById(contratoId)).thenReturn(Optional.of(contrato));
        when(mensalidadeRepository.existsByContratoIdAndMesReferencia(
                contratoId,
                request.mesReferencia()
        )).thenReturn(true);

        assertThrows(
                IllegalArgumentBusinessException.class,
                () -> mensalidadeService.criar(contratoId, request)
        );

        verify(mensalidadeRepository, never()).save(any(Mensalidade.class));
    }

    @Test
    void deveRegistrarPagamentoManual() {
        UUID mensalidadeId = UUID.randomUUID();
        LocalDate dataPagamento = LocalDate.now();

        Mensalidade mensalidade = new Mensalidade();
        mensalidade.setId(mensalidadeId);
        mensalidade.setStatus(MensalidadeStatus.PENDENTE);

        when(mensalidadeRepository.findById(mensalidadeId))
                .thenReturn(Optional.of(mensalidade));
        when(mensalidadeRepository.save(any(Mensalidade.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Mensalidade atualizada = mensalidadeService.registrarPagamento(
                mensalidadeId,
                new RegistrarPagamentoRequest(dataPagamento)
        );

        assertEquals(MensalidadeStatus.PAGO, atualizada.getStatus());
        assertEquals(dataPagamento, atualizada.getDataPagamento());
    }

    @Test
    void deveCalcularResumoFinanceiro() {
        Mensalidade paga = mensalidade(
                new BigDecimal("100.00"),
                MensalidadeStatus.PAGO,
                LocalDate.now().minusDays(3)
        );
        Mensalidade pendente = mensalidade(
                new BigDecimal("200.00"),
                MensalidadeStatus.PENDENTE,
                LocalDate.now().plusDays(5)
        );
        Mensalidade vencida = mensalidade(
                new BigDecimal("300.00"),
                MensalidadeStatus.VENCIDO,
                LocalDate.now().minusDays(2)
        );

        when(mensalidadeRepository.findAllByOrderByVencimentoDesc())
                .thenReturn(new ArrayList<>(List.of(paga, pendente, vencida)));

        FinanceiroResumoResponse resumo = mensalidadeService.resumo();

        assertEquals(new BigDecimal("600.00"), resumo.receitaPrevista());
        assertEquals(new BigDecimal("100.00"), resumo.receitaRecebida());
        assertEquals(new BigDecimal("500.00"), resumo.valorEmAberto());
        assertEquals(new BigDecimal("300.00"), resumo.valorVencido());
        assertEquals(1, resumo.mensalidadesPagas());
        assertEquals(1, resumo.mensalidadesPendentes());
        assertEquals(1, resumo.mensalidadesVencidas());
    }

    private Mensalidade mensalidade(
            BigDecimal valor,
            MensalidadeStatus status,
            LocalDate vencimento
    ) {
        Mensalidade mensalidade = new Mensalidade();
        mensalidade.setId(UUID.randomUUID());
        mensalidade.setValor(valor);
        mensalidade.setStatus(status);
        mensalidade.setVencimento(vencimento);
        mensalidade.setMesReferencia(YearMonth.of(2026, 10));
        return mensalidade;
    }
}
