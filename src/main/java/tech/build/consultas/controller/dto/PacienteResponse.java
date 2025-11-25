package tech.build.consultas.controller.dto;

import java.time.LocalDate;

public record PacienteResponse(
        String pacienteNome,
        String pacienteEmail,
        String pacienteTelefone,
        LocalDate dataNascimento,
        ResponsavelResponse responsavel
) {
    public record ResponsavelResponse(
            String nome,
            String email,
            String telefone
    ) {}
}