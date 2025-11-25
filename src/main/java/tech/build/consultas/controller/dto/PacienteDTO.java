package tech.build.consultas.controller.dto;

import java.time.LocalDate;

public record PacienteDTO(
        String pacienteCpf,
        String pacienteNome,
        String pacienteEmail,
        String pacienteTelefone,
        LocalDate dataNascimento,
        EnderecoDTO endereco,
        ResponsavelDTO responsavel) {
}