package com.glp.client_portal.economia;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface EconomiaRepository extends JpaRepository<Economia, UUID> {

    List<Economia> findByContratoId(UUID contratoId);

    List<Economia> findByContratoClienteId(UUID clienteId);
}
