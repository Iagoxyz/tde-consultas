package tech.build.consultas.controller.dto;

public record EnderecoDTO(
        String estado,
        String cidade,
        String bairro,
        String cep,
        String rua,
        String numero
) {
}