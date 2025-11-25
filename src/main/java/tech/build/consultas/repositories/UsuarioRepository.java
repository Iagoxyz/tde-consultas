package tech.build.consultas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.build.consultas.entities.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);
}