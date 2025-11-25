package tech.build.consultas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.build.consultas.entities.Paciente;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
}