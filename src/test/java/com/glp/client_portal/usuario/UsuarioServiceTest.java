package com.glp.client_portal.usuario;

import com.glp.client_portal.cliente.Cliente;
import com.glp.client_portal.cliente.ClienteService;
import com.glp.client_portal.exception.IllegalArgumentBusinessException;
import com.glp.client_portal.usuario.dto.AlterarSenhaRequest;
import com.glp.client_portal.usuario.dto.CriarUsuarioRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private ClienteService clienteService;
    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void deveCadastrarAdministradorSemCliente() {
        CriarUsuarioRequest request = new CriarUsuarioRequest(
                "admin@glp.com", "senha", Role.ADMIN, null
        );
        when(usuarioRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode("senha")).thenReturn("hash");
        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario usuario = usuarioService.cadastrar(request);

        assertEquals(Role.ADMIN, usuario.getRole());
        assertNull(usuario.getCliente());
        assertEquals("hash", usuario.getSenha());
    }

    @Test
    void deveCadastrarUsuarioClienteVinculado() {
        UUID clienteId = UUID.randomUUID();
        Cliente cliente = new Cliente();
        cliente.setId(clienteId);
        CriarUsuarioRequest request = new CriarUsuarioRequest(
                "cliente@glp.com", "senha", Role.CLIENTE, clienteId
        );

        when(usuarioRepository.existsByEmail(request.email())).thenReturn(false);
        when(usuarioRepository.existsByClienteId(clienteId)).thenReturn(false);
        when(passwordEncoder.encode("senha")).thenReturn("hash");
        when(clienteService.buscarPorId(clienteId)).thenReturn(cliente);
        when(usuarioRepository.save(any(Usuario.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Usuario usuario = usuarioService.cadastrar(request);

        assertSame(cliente, usuario.getCliente());
        assertEquals(Role.CLIENTE, usuario.getRole());
    }

    @Test
    void naoDeveCadastrarSegundoAcessoParaMesmoCliente() {
        UUID clienteId = UUID.randomUUID();
        CriarUsuarioRequest request = new CriarUsuarioRequest(
                "outro@glp.com", "Senha123", Role.CLIENTE, clienteId
        );

        when(usuarioRepository.existsByEmail(request.email())).thenReturn(false);
        when(usuarioRepository.existsByClienteId(clienteId)).thenReturn(true);

        IllegalArgumentBusinessException exception = assertThrows(
                IllegalArgumentBusinessException.class,
                () -> usuarioService.cadastrar(request)
        );

        assertEquals("Este cliente já possui um acesso ao portal", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
        verifyNoInteractions(clienteService);
    }

    @Test
    void naoDeveCadastrarEmailDuplicado() {
        CriarUsuarioRequest request = new CriarUsuarioRequest(
                "duplicado@glp.com", "senha", Role.ADMIN, null
        );
        when(usuarioRepository.existsByEmail(request.email())).thenReturn(true);

        assertThrows(IllegalArgumentBusinessException.class,
                () -> usuarioService.cadastrar(request));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void clienteDeveTerClienteVinculado() {
        CriarUsuarioRequest request = new CriarUsuarioRequest(
                "cliente@glp.com", "senha", Role.CLIENTE, null
        );
        when(usuarioRepository.existsByEmail(request.email())).thenReturn(false);
        when(passwordEncoder.encode("senha")).thenReturn("hash");

        assertThrows(IllegalArgumentBusinessException.class,
                () -> usuarioService.cadastrar(request));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void deveAlterarSenhaQuandoSenhaAtualConfere() {
        Usuario usuario = new Usuario();
        usuario.setEmail("cliente@glp.com");
        usuario.setSenha("hash-antigo");
        AlterarSenhaRequest request = new AlterarSenhaRequest("antiga", "nova");

        when(usuarioRepository.findByEmail(usuario.getEmail()))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("antiga", "hash-antigo")).thenReturn(true);
        when(passwordEncoder.encode("nova")).thenReturn("hash-novo");

        usuarioService.alterarSenha(usuario.getEmail(), request);

        assertEquals("hash-novo", usuario.getSenha());
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void naoDeveAlterarSenhaQuandoSenhaAtualEstaIncorreta() {
        Usuario usuario = new Usuario();
        usuario.setSenha("hash");
        AlterarSenhaRequest request = new AlterarSenhaRequest("errada", "nova");

        when(usuarioRepository.findByEmail("cliente@glp.com"))
                .thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("errada", "hash")).thenReturn(false);

        assertThrows(IllegalArgumentBusinessException.class,
                () -> usuarioService.alterarSenha("cliente@glp.com", request));
        verify(usuarioRepository, never()).save(any());
    }
}
