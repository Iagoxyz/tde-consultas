package tech.build.consultas.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.build.consultas.controller.dto.UsuarioResponse;
import tech.build.consultas.entities.Usuario;
import tech.build.consultas.entities.UsuarioTipo;
import tech.build.consultas.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // CONSULTAR POR EMAIL (restrito)
    @Transactional(readOnly = true)
    public UsuarioResponse buscarPorEmail(String emailConsulta, Authentication auth) {

        String emailLogado = auth.getName();

        Usuario usuarioLogado = usuarioRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado."));

        // Usuário só pode buscar ele mesmo
        // ADMIN pode buscar qualquer um
        if (!usuarioLogado.getEmail().equalsIgnoreCase(emailConsulta)
                && usuarioLogado.getTipo() != UsuarioTipo.ADMIN) {
            throw new SecurityException("Acesso negado: somente ADMIN pode consultar outros usuários.");
        }

        Usuario alvo = usuarioRepository.findByEmail(emailConsulta)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com email: " + emailConsulta));

        return toResponse(alvo);
    }

    // LISTAR PAGINADO (apenas ADMIN)
    @Transactional(readOnly = true)
    public Page<UsuarioResponse> listarUsuarios(Pageable pageable, Authentication auth) {

        String emailLogado = auth.getName();
        Usuario usuarioLogado = usuarioRepository.findByEmail(emailLogado)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado."));

        if (usuarioLogado.getTipo() != UsuarioTipo.ADMIN) {
            throw new SecurityException("Acesso negado: apenas ADMIN pode listar usuários.");
        }

        return usuarioRepository.findAll(pageable).map(this::toResponse);
    }

    private UsuarioResponse toResponse(Usuario u) {
        return new UsuarioResponse(
                u.getUserId(),
                u.getNome(),
                u.getEmail(),
                u.getTipo() != null ? u.getTipo().name() : null
        );
    }
}

