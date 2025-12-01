package tech.build.consultas.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tech.build.consultas.controller.dto.UsuarioResponse;
import tech.build.consultas.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    // CONSULTA POR EMAIL
    @GetMapping
    public ResponseEntity<?> buscarPorEmail(@RequestParam(required = false) String email,
                                            Authentication authentication) {
        try {
            if (email == null || email.isBlank()) {
                return ResponseEntity.badRequest().body("Parâmetro 'email' é obrigatório.");
            }
            UsuarioResponse resp = usuarioService.buscarPorEmail(email, authentication);
            return ResponseEntity.ok(resp);

        } catch (SecurityException se) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(se.getMessage());
        } catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(re.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }

    // LISTAR TODOS (APENAS ADMIN)
    @GetMapping("/all")
    public ResponseEntity<?> listarUsuarios(Pageable pageable, Authentication authentication) {
        try {
            Page<UsuarioResponse> page = usuarioService.listarUsuarios(pageable, authentication);
            return ResponseEntity.ok(page);

        } catch (SecurityException se) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(se.getMessage());
        } catch (RuntimeException re) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(re.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno: " + e.getMessage());
        }
    }
}
