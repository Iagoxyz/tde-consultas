package tech.build.consultas.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.build.consultas.controller.dto.AuthRequest;
import tech.build.consultas.entities.Usuario;
import tech.build.consultas.entities.UsuarioTipo;
import tech.build.consultas.repositories.UsuarioRepository;
import tech.build.consultas.security.JwtService;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Usuario registrar(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        // Define tipo padrão se não for informado
        if (usuario.getTipo() == null) {
            usuario.setTipo(UsuarioTipo.DEFAULT);
        }

        // Salva e retorna o usuário persistido
        return usuarioRepository.save(usuario);
    }


    public String login(AuthRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(request.senha(), usuario.getSenha())) {
            throw new RuntimeException("Credenciais inválidas");
        }

        return jwtService.gerarToken(usuario.getEmail(), usuario.getTipo().name());
    }
}