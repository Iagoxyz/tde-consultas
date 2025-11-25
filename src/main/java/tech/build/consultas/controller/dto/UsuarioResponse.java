package tech.build.consultas.controller.dto;

public record UsuarioResponse(Long id,
                              String name,
                              String email,
                              String tipoUser) {
}
