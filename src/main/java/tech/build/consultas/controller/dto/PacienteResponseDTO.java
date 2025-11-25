package tech.build.consultas.controller.dto;

import java.time.LocalDate;

public record PacienteResponseDTO(
        Long id,
        String nome,
        String email,
        String telefone,
        LocalDate dataNascimento
) {
}
