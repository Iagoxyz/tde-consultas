package tech.build.consultas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.build.consultas.entities.Paciente;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    Optional<Paciente> findByPacienteCpf(String pacienteCpf);
    Optional<Paciente> findByPacienteEmail(String pacienteEmail);
    boolean existsByPacienteCpf(String pacienteCpf);
    boolean existsByPacienteEmail(String pacienteEmail);
}