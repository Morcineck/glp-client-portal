package com.glp.client_portal.usuario.auth.security;

import com.glp.client_portal.usuario.Role;
import com.glp.client_portal.usuario.Usuario;
import com.glp.client_portal.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AccessValidator {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public void validarAcessoCliente(UserDetails userDetails, UUID clienteId) {
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new AccessDeniedException("Usuário não encontrado"));

        if (usuario.getRole() == Role.ADMIN) {
            return; // ADMIN tem acesso total
        }

        if (usuario.getCliente() == null ||
                !usuario.getCliente().getId().equals(clienteId)) {
            throw new AccessDeniedException("Você não tem permissão para acessar dados deste cliente");
        }

    }

    public UUID getClienteIdUsuario(UserDetails userDetails) {
        Usuario usuario = usuarioRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new AccessDeniedException("Usuário não encontrado"));

        if (usuario.getRole() == Role.ADMIN) {
            return null; // ADMIN não tem clienteId vinculado
        }

        if (usuario.getCliente() == null) {
            throw new AccessDeniedException("Usuário CLIENTE sem cliente vinculado");
        }

        return usuario.getCliente().getId();
    }
}
