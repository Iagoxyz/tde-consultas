package tech.build.consultas.controller.dto;

import java.time.LocalDate;

public record ResponsavelDTO(
        String cpf,
        String nome,
        LocalDate dataNascimento,
        String email,
        String telefone) {
}