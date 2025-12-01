package tech.build.consultas.controller.dto;

import tech.build.consultas.entities.Procedimento;

import java.math.BigDecimal;

public record ProcedimentoResponseDTO(
        Long id,
        String nome,
        String descricao,
        BigDecimal valorPlan,
        BigDecimal valorParticular
) {
    public static ProcedimentoResponseDTO fromEntity(Procedimento p) {
        return new ProcedimentoResponseDTO(
                p.getProcedimentoId(),
                p.getNome(),
                p.getDescricao(),
                p.getValorPlan(),
                p.getValorParticular()
        );
    }
}