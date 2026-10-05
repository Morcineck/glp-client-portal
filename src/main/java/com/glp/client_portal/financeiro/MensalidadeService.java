package com.glp.client_portal.financeiro;

import com.glp.client_portal.contrato.Contrato;
import com.glp.client_portal.contrato.ContratoRepository;
import com.glp.client_portal.exception.IllegalArgumentBusinessException;
import com.glp.client_portal.exception.ResourceNotFoundException;
import com.glp.client_portal.financeiro.dto.CriarMensalidadeRequest;
import com.glp.client_portal.financeiro.dto.FinanceiroResumoResponse;
import com.glp.client_portal.financeiro.dto.RegistrarPagamentoRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class MensalidadeService {

    private final MensalidadeRepository mensalidadeRepository;
    private final ContratoRepository contratoRepository;

    public MensalidadeService(
            MensalidadeRepository mensalidadeRepository,
            ContratoRepository contratoRepository
    ) {
        this.mensalidadeRepository = mensalidadeRepository;
        this.contratoRepository = contratoRepository;
    }

    public Mensalidade criar(UUID contratoId, CriarMensalidadeRequest request) {
        Contrato contrato = contratoRepository.findById(contratoId)
                .orElseThrow(() -> new ResourceNotFoundException("Contrato não encontrado"));

        if (mensalidadeRepository.existsByContratoIdAndMesReferencia(
                contratoId,
                request.mesReferencia()
        )) {
            throw new IllegalArgumentBusinessException(
                    "Já existe uma mensalidade para este contrato na competência "
                            + request.mesReferencia()
            );
        }

        Mensalidade mensalidade = new Mensalidade();
        mensalidade.setContrato(contrato);
        mensalidade.setMesReferencia(request.mesReferencia());
        mensalidade.setVencimento(request.vencimento());
        mensalidade.setValor(
                request.valor() != null ? request.valor() : contrato.getValorMensal()
        );
        mensalidade.setStatus(
                request.vencimento().isBefore(LocalDate.now())
                        ? MensalidadeStatus.VENCIDO
                        : MensalidadeStatus.PENDENTE
        );

        return mensalidadeRepository.save(mensalidade);
    }

    public List<Mensalidade> listar() {
        List<Mensalidade> mensalidades = mensalidadeRepository.findAllByOrderByVencimentoDesc();
        atualizarVencidos(mensalidades);
        return mensalidades;
    }

    public Mensalidade registrarPagamento(UUID mensalidadeId, RegistrarPagamentoRequest request) {
        Mensalidade mensalidade = mensalidadeRepository.findById(mensalidadeId)
                .orElseThrow(() -> new ResourceNotFoundException("Mensalidade não encontrada"));

        mensalidade.setDataPagamento(
                request.dataPagamento() != null ? request.dataPagamento() : LocalDate.now()
        );
        mensalidade.setStatus(MensalidadeStatus.PAGO);

        return mensalidadeRepository.save(mensalidade);
    }

    public FinanceiroResumoResponse resumo() {
        List<Mensalidade> mensalidades = listar();

        BigDecimal receitaPrevista = somar(mensalidades);
        BigDecimal receitaRecebida = somarPorStatus(mensalidades, MensalidadeStatus.PAGO);
        BigDecimal valorVencido = somarPorStatus(mensalidades, MensalidadeStatus.VENCIDO);
        BigDecimal valorEmAberto = mensalidades.stream()
                .filter(item -> item.getStatus() != MensalidadeStatus.PAGO)
                .map(Mensalidade::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long pagas = contar(mensalidades, MensalidadeStatus.PAGO);
        long pendentes = contar(mensalidades, MensalidadeStatus.PENDENTE);
        long vencidas = contar(mensalidades, MensalidadeStatus.VENCIDO);

        return new FinanceiroResumoResponse(
                receitaPrevista,
                receitaRecebida,
                valorEmAberto,
                valorVencido,
                pagas,
                pendentes,
                vencidas
        );
    }

    private void atualizarVencidos(List<Mensalidade> mensalidades) {
        mensalidades.stream()
                .filter(item -> item.getStatus() == MensalidadeStatus.PENDENTE)
                .filter(item -> item.getVencimento().isBefore(LocalDate.now()))
                .forEach(item -> item.setStatus(MensalidadeStatus.VENCIDO));

        mensalidadeRepository.saveAll(mensalidades);
    }

    private BigDecimal somar(List<Mensalidade> mensalidades) {
        return mensalidades.stream()
                .map(Mensalidade::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal somarPorStatus(
            List<Mensalidade> mensalidades,
            MensalidadeStatus status
    ) {
        return mensalidades.stream()
                .filter(item -> item.getStatus() == status)
                .map(Mensalidade::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private long contar(List<Mensalidade> mensalidades, MensalidadeStatus status) {
        return mensalidades.stream()
                .filter(item -> item.getStatus() == status)
                .count();
    }
}
