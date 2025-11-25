package tech.build.consultas.controller.dto;

import java.time.LocalDate;

public record PacienteUpdateDTO(
        String nome,
        String email,
        String telefone,
        LocalDate dataNascimento
) {
}
