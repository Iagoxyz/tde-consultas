package tech.build.consultas.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

public record AtendimentoDTO(
        LocalDateTime dateTime,
        Long pacienteId,
        List<Long> procedimentosIds,
        String tipo,          // "PLANO" ou "PARTICULAR"
        String planNumber,    // opcional
        Long usuarioId        // usuário que está criando o atendimento
) { }
