package com.glp.client_portal.usuario;

import com.glp.client_portal.cliente.Cliente;
import com.glp.client_portal.cliente.ClienteService;
import com.glp.client_portal.exception.IllegalArgumentBusinessException;
import com.glp.client_portal.exception.ResourceNotFoundException;
import com.glp.client_portal.usuario.dto.AlterarSenhaRequest;
import com.glp.client_portal.usuario.dto.CriarUsuarioRequest;
import com.glp.client_portal.usuario.dto.UsuarioResumoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
        usuario.setRole(request.role());

        if (request.role() == Role.CLIENTE) {
            if (request.clienteId() == null) {
                throw new IllegalArgumentBusinessException(
                        "Usuário do tipo CLIENTE precisa estar vinculado a um cliente");
            }
            if (usuarioRepository.existsByClienteId(request.clienteId())) {
                throw new IllegalArgumentBusinessException(
                        "Este cliente já possui um acesso ao portal"
                );
            }

            Cliente cliente = clienteService.buscarPorId(request.clienteId());
            usuario.setCliente(cliente);
        }

        usuario.setSenha(passwordEncoder.encode(request.senha()));
        return usuarioRepository.save(usuario);
    }

    public List<UsuarioResumoResponse> listarResumos() {
        return usuarioRepository.findAll().stream()
                .map(usuario -> new UsuarioResumoResponse(
                        usuario.getId(),
                        usuario.getEmail(),
                        usuario.getRole(),
                        usuario.getCliente() != null ? usuario.getCliente().getId() : null
                ))
                .toList();
    }

    public void alterarSenha(String emailDoToken, AlterarSenhaRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(emailDoToken)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.senhaAtual(), usuario.getSenha())) {
            throw new IllegalArgumentBusinessException("Senha atual incorreta!");
        }

        usuario.setSenha(passwordEncoder.encode(request.novaSenha()));
        usuarioRepository.save(usuario);
    }

}

