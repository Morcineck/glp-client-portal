package com.glp.client_portal.financeiro;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

public interface MensalidadeRepository extends JpaRepository<Mensalidade, UUID> {

    List<Mensalidade> findAllByOrderByVencimentoDesc();

    boolean existsByContratoIdAndMesReferencia(UUID contratoId, YearMonth mesReferencia);
}
