package tech.build.consultas.controller.dto;

import tech.build.consultas.entities.Paciente;

import java.time.LocalDate;

public record PacienteResponseDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        LocalDate dataNascimento
) {

    public static PacienteResponseDTO fromEntity(Paciente paciente) {
        return new PacienteResponseDTO(
                paciente.getPacienteId(),
                paciente.getPacienteNome(),
                paciente.getPacienteEmail(),
                paciente.getPacienteTelefone(),
                paciente.getDataNascimento()
        );
    }
}