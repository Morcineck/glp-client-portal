package com.glp.client_portal.usuario.auth.security;

import com.glp.client_portal.cliente.Cliente;
import com.glp.client_portal.contrato.Contrato;
import com.glp.client_portal.contrato.ContratoRepository;
import com.glp.client_portal.usuario.Role;
import com.glp.client_portal.usuario.Usuario;
import com.glp.client_portal.usuario.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessValidatorTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private ContratoRepository contratoRepository;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AccessValidator accessValidator;

    @Test
    void adminDeveTerAcessoAQualquerCliente() {
        Usuario admin = new Usuario();
        admin.setRole(Role.ADMIN);

        when(userDetails.getUsername()).thenReturn("admin@glp.com");
        when(usuarioRepository.findByEmail("admin@glp.com"))
                .thenReturn(Optional.of(admin));

        assertDoesNotThrow(() ->
                accessValidator.validarAcessoCliente(userDetails, UUID.randomUUID())
        );
    }

    @Test
    void clienteNaoDeveAcessarOutroCliente() {
        UUID clienteId = UUID.randomUUID();
        UUID outroClienteId = UUID.randomUUID();

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        Usuario usuario = new Usuario();
        usuario.setRole(Role.CLIENTE);
        usuario.setCliente(cliente);

        when(userDetails.getUsername()).thenReturn("cliente@glp.com");
        when(usuarioRepository.findByEmail("cliente@glp.com"))
                .thenReturn(Optional.of(usuario));

        assertThrows(
                AccessDeniedException.class,
                () -> accessValidator.validarAcessoCliente(userDetails, outroClienteId)
        );
    }

    @Test
    void deveRecusarContratoDeOutroCliente() {
        UUID clienteId = UUID.randomUUID();
        UUID outroClienteId = UUID.randomUUID();
        UUID contratoId = UUID.randomUUID();

        Cliente outroCliente = new Cliente();
        outroCliente.setId(outroClienteId);

        Contrato contrato = new Contrato();
        contrato.setId(contratoId);
        contrato.setCliente(outroCliente);

        when(contratoRepository.findById(contratoId))
                .thenReturn(Optional.of(contrato));

        assertThrows(
                AccessDeniedException.class,
                () -> accessValidator.validarContratoDoCliente(clienteId, contratoId)
        );
    }

    @Test
    void deveAceitarContratoDoMesmoCliente() {
        UUID clienteId = UUID.randomUUID();
        UUID contratoId = UUID.randomUUID();

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        Contrato contrato = new Contrato();
        contrato.setId(contratoId);
        contrato.setCliente(cliente);

        when(contratoRepository.findById(contratoId))
                .thenReturn(Optional.of(contrato));

        assertDoesNotThrow(() ->
                accessValidator.validarContratoDoCliente(clienteId, contratoId)
        );
    }

    @Test
    void deveRetornarClienteVinculadoAoUsuarioCliente() {
        UUID clienteId = UUID.randomUUID();

        Cliente cliente = new Cliente();
        cliente.setId(clienteId);

        Usuario usuario = new Usuario();
        usuario.setRole(Role.CLIENTE);
        usuario.setCliente(cliente);

        when(userDetails.getUsername()).thenReturn("cliente@glp.com");
        when(usuarioRepository.findByEmail("cliente@glp.com"))
                .thenReturn(Optional.of(usuario));

        assertEquals(clienteId, accessValidator.getClienteIdUsuario(userDetails));
    }
}
