package com.glp.client_portal.usuario;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    boolean existsByEmail(String email);

    boolean existsByClienteId(UUID clienteId);

    @EntityGraph(attributePaths = "cliente")
    java.util.List<Usuario> findAll();

    Optional<Usuario> findByEmail(String email);


}
