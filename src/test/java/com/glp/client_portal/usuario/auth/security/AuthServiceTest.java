package com.glp.client_portal.usuario.auth.security;

import com.glp.client_portal.exception.ResourceNotFoundException;
import com.glp.client_portal.usuario.Role;
import com.glp.client_portal.usuario.Usuario;
import com.glp.client_portal.usuario.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtUtil jwtUtil;
    @InjectMocks
    private AuthService authService;

    @Test
    void deveGerarTokenQuandoCredenciaisSaoValidas() {
        Usuario usuario = new Usuario();
        usuario.setEmail("admin@glp.com");
        usuario.setSenha("hash");
        usuario.setRole(Role.ADMIN);

        when(usuarioRepository.findByEmail("admin@glp.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha", "hash")).thenReturn(true);
        when(jwtUtil.generateToken("admin@glp.com", "ADMIN")).thenReturn("jwt-token");

        assertEquals("jwt-token", authService.login("admin@glp.com", "senha"));
    }

    @Test
    void naoDeveAutenticarEmailInexistente() {
        when(usuarioRepository.findByEmail("inexistente@glp.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> authService.login("inexistente@glp.com", "senha"));
        verifyNoInteractions(passwordEncoder, jwtUtil);
    }

    @Test
    void naoDeveAutenticarSenhaIncorreta() {
        Usuario usuario = new Usuario();
        usuario.setSenha("hash");

        when(usuarioRepository.findByEmail("cliente@glp.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("errada", "hash")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class,
                () -> authService.login("cliente@glp.com", "errada"));
        verifyNoInteractions(jwtUtil);
    }
}
