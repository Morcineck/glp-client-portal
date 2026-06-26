package com.glp.client_portal.contrato;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContratoRepository extends JpaRepository<Contrato, UUID> {

    List<Contrato> findByClienteId(UUID clienteId);
}
