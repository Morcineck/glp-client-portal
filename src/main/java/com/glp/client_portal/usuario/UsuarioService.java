package com.glp.client_portal.usuario;

import com.glp.client_portal.cliente.Cliente;
import com.glp.client_portal.cliente.ClienteService;
import com.glp.client_portal.exception.IllegalArgumentBusinessException;
import com.glp.client_portal.exception.ResourceNotFoundException;
import com.glp.client_portal.usuario.dto.AlterarSenhaRequest;
import com.glp.client_portal.usuario.dto.CriarUsuarioRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ClienteService clienteService;


    public Usuario cadastrar(CriarUsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentBusinessException("Usuário já cadastrado com esse e-mail");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setRole(request.role());


        if (request.role() == Role.CLIENTE) {
            if (request.clienteId() == null) {
                throw new IllegalArgumentBusinessException(
                        "Usuário do tipo CLIENTE precisa estar vinculado a um cliente");
            }
            Cliente cliente = clienteService.buscarPorId(request.clienteId());
            usuario.setCliente(cliente);
        }

        return usuarioRepository.save(usuario);
    }

    public void alterarSenha(String emailDoToken, AlterarSenhaRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(emailDoToken)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.senhaAtual(), usuario.getSenha())) {
            throw new IllegalArgumentException("Senha atual incorreta!");
        }

        usuario.setSenha(passwordEncoder.encode(request.novaSenha()));
        usuarioRepository.save(usuario);
    }

}

