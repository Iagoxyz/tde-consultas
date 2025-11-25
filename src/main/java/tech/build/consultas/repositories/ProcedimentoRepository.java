package tech.build.consultas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.build.consultas.entities.Procedimento;

public interface ProcedimentoRepository extends JpaRepository<Procedimento, Long> {
}
