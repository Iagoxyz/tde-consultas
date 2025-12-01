package tech.build.consultas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.build.consultas.entities.Procedimento;

import java.util.Optional;

public interface ProcedimentoRepository extends JpaRepository<Procedimento, Long> {
    Optional<Procedimento> findByNome(String nome);
    boolean existsByNome(String nome);
}
