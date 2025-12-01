package tech.build.consultas.controller.dto;

import tech.build.consultas.entities.Paciente;

import java.time.LocalDate;

public record PacienteResponse(
        String pacienteNome,
        String pacienteEmail,
        String pacienteTelefone,
        LocalDate dataNascimento,
        ResponsavelResponse responsavel
) {

    public static PacienteResponse fromEntity(Paciente paciente) {
        return new PacienteResponse(
                paciente.getPacienteNome(),
                paciente.getPacienteEmail(),
                paciente.getPacienteTelefone(),
                paciente.getDataNascimento(),
                paciente.getResponsavel() != null ?
                        new ResponsavelResponse(
                                paciente.getResponsavel().getNome(),
                                paciente.getResponsavel().getEmail(),
                                paciente.getResponsavel().getTelefone()
                        )
                        : null
        );
    }

    public record ResponsavelResponse(
            String nome,
            String email,
            String telefone
    ) {}
}