package tech.build.consultas.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.build.consultas.controller.dto.AuthRequest;
import tech.build.consultas.controller.dto.AuthResponse;
import tech.build.consultas.controller.dto.UsuarioResponse;
import tech.build.consultas.entities.Usuario;
import tech.build.consultas.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 🔹 Endpoint para registrar usuário
    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody Usuario usuario) {
        try {
            Usuario novoUsuario = authService.registrar(usuario);
            UsuarioResponse resposta = new UsuarioResponse(
                    novoUsuario.getUserId(),
                    novoUsuario.getNome(),
                    novoUsuario.getEmail(),
                    novoUsuario.getTipo().name()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body("Erro ao registrar usuário: " + e.getMessage());
        }
    }

    // 🔹 Endpoint para login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            String token = authService.login(request);
            return ResponseEntity.ok(new AuthResponse(token));
        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Erro de autenticação: " + e.getMessage());
        }
    }
}
