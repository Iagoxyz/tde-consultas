package tech.build.consultas.controller.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AtendimentoResponse(
        Long id,
        LocalDateTime dateTime,
        String pacienteNome,
        List<String> procedimentos,
        String tipo,
        String planNumber,
        String usuarioNome,
        BigDecimal valorTotal
) { }
