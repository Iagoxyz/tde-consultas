package tech.build.consultas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.build.consultas.entities.Atendimento;

public interface AtendimentoRepository extends JpaRepository<Atendimento, Long> {

}
