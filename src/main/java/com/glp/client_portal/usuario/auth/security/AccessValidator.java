package com.glp.client_portal.usuario.auth.security;

import com.glp.client_portal.contrato.Contrato;
import com.glp.client_portal.contrato.ContratoRepository;
import com.glp.client_portal.exception.ResourceNotFoundException;
import com.glp.client_portal.usuario.Role;
import com.glp.client_portal.usuario.Usuario;
import com.glp.client_portal.usuario.UsuarioRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccessValidator {

    private final UsuarioRepository usuarioRepository;
    private final ContratoRepository contratoRepository;

    public AccessValidator(
            UsuarioRepository usuarioRepository,
            ContratoRepository contratoRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.contratoRepository = contratoRepository;
    }

    public void validarAcessoCliente(UserDetails userDetails, UUID clienteId) {
        Usuario usuario = buscarUsuario(userDetails);

        if (usuario.getRole() == Role.ADMIN) {
            return;
        }

        if (usuario.getCliente() == null
                || !usuario.getCliente().getId().equals(clienteId)) {
            throw new AccessDeniedException(
                    "Você não tem permissão para acessar dados deste cliente"
            );
        }
    }

    public void validarContratoDoCliente(UUID clienteId, UUID contratoId) {
        Contrato contrato = contratoRepository.findById(contratoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Contrato com ID [" + contratoId + "] não existe na base de dados."
                ));

        if (!contrato.getCliente().getId().equals(clienteId)) {
            throw new AccessDeniedException(
                    "O contrato informado não pertence ao cliente"
            );
        }
    }

    public UUID getClienteIdUsuario(UserDetails userDetails) {
        Usuario usuario = buscarUsuario(userDetails);

        if (usuario.getRole() == Role.ADMIN) {
            return null;
        }

        if (usuario.getCliente() == null) {
            throw new AccessDeniedException(
                    "Usuário CLIENTE sem cliente vinculado"
            );
        }

        return usuario.getCliente().getId();
    }

    private Usuario buscarUsuario(UserDetails userDetails) {
        return usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new AccessDeniedException(
                        "Usuário não encontrado"
                ));
    }
}
