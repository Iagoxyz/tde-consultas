package tech.build.consultas.controller.dto;

import java.math.BigDecimal;

public record ProcedimentoRequestDTO(
        String nome,
        String descricao,
        BigDecimal valorPlan,
        BigDecimal valorParticular
) {}