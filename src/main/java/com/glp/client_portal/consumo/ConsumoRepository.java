package com.glp.client_portal.consumo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ConsumoRepository extends JpaRepository<ConsumoMensal, UUID> {

    List<ConsumoMensal> findByContratoId(UUID contrato);
}
