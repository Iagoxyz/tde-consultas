package tech.build.consultas.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import tech.build.consultas.controller.dto.AtendimentoDTO;
import tech.build.consultas.controller.dto.AtendimentoResponse;
import tech.build.consultas.entities.*;
import tech.build.consultas.repositories.AtendimentoRepository;
import tech.build.consultas.repositories.PacienteRepository;
import tech.build.consultas.repositories.ProcedimentoRepository;
import tech.build.consultas.repositories.UsuarioRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AtendimentoService {

    private final AtendimentoRepository atendimentoRepository;
    private final PacienteRepository pacienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProcedimentoRepository procedimentoRepository;

    public AtendimentoService(
            AtendimentoRepository atendimentoRepository,
            PacienteRepository pacienteRepository,
            UsuarioRepository usuarioRepository,
            ProcedimentoRepository procedimentoRepository
    ) {
        this.atendimentoRepository = atendimentoRepository;
        this.pacienteRepository = pacienteRepository;
        this.usuarioRepository = usuarioRepository;
        this.procedimentoRepository = procedimentoRepository;
    }

    @Transactional
    public AtendimentoResponse criar(AtendimentoDTO dto) {

        AtendimentoTipo tipo = AtendimentoTipo.valueOf(dto.tipo().toUpperCase());

        if (dto.procedimentosIds() == null || dto.procedimentosIds().isEmpty()) {
            throw new RuntimeException("O atendimento deve possuir ao menos 1 procedimento.");
        }

        if (tipo == AtendimentoTipo.PLANO && (dto.planNumber() == null || dto.planNumber().isBlank())) {
            throw new RuntimeException("Número do plano é obrigatório para atendimentos do tipo PLANO.");
        }

        Paciente paciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        List<Procedimento> procedimentos =
                procedimentoRepository.findAllById(dto.procedimentosIds());

        Atendimento atendimento = new Atendimento();
        atendimento.setDateTime(dto.dateTime());
        atendimento.setPaciente(paciente);
        atendimento.setUsuario(usuario);
        atendimento.setProcedimentos(procedimentos);
        atendimento.setTipo(tipo);
        atendimento.setPlanNumber(dto.planNumber());

        // Cálculo do valor total
        BigDecimal total = BigDecimal.ZERO;
        for (Procedimento p : procedimentos) {
            if (tipo == AtendimentoTipo.PLANO) {
                total = total.add(p.getValorPlan());
            } else {
                total = total.add(p.getValorParticular());
            }
        }
        atendimento.setValorTotal(total);

        Atendimento saved = atendimentoRepository.save(atendimento);

        return new AtendimentoResponse(
                saved.getId(),
                saved.getDateTime(),
                saved.getPaciente().getPacienteNome(),
                saved.getProcedimentos().stream().map(Procedimento::getNome).toList(),
                saved.getTipo().name(),
                saved.getPlanNumber(),
                saved.getUsuario().getNome(),
                saved.getValorTotal()
        );
    }
}
