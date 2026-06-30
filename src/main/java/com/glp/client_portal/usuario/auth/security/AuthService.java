package com.glp.client_portal.usuario.auth.security;

import com.glp.client_portal.exception.ResourceNotFoundException;
import com.glp.client_portal.usuario.Usuario;
import com.glp.client_portal.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public String login(String email, String senha){
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email ou senha inválidos"));

        if(!passwordEncoder.matches(senha, usuario.getSenha())){
            throw new ResourceNotFoundException("Email ou senha inválidos");
        }

        return jwtUtil.generateToken(usuario.getEmail(), usuario.getRole().name());
    }
}
