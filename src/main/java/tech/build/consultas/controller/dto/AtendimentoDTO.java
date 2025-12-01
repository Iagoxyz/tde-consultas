package tech.build.consultas.controller.dto;

import java.time.LocalDateTime;
import java.util.List;

public record AtendimentoDTO(
        LocalDateTime dateTime,
        Long pacienteId,
        Long usuarioId,
        List<Long> procedimentosIds,
        String tipo,           // "PLANO" ou "PARTICULAR"
        String planNumber      // opcional
) {}

