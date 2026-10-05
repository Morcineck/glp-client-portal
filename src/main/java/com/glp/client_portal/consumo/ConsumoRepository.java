package com.glp.client_portal.consumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.UUID;

@Repository
public interface ConsumoRepository extends JpaRepository<ConsumoMensal, UUID> {

    List<ConsumoMensal> findByContratoId(UUID contratoId);

    List<ConsumoMensal> findByContratoIdOrderByMesReferenciaAsc(UUID contratoId);

    boolean existsByContratoIdAndMesReferencia(UUID contratoId, YearMonth mesReferencia);
}
