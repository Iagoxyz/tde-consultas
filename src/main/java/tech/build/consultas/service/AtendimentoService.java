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

    // ------------------------
    // CRIAR
    // ------------------------
    @Transactional
    public AtendimentoResponse criar(AtendimentoDTO dto) {

        AtendimentoTipo tipo = AtendimentoTipo.valueOf(dto.tipo().toUpperCase());

        // Deve possuir ao menos 1 procedimento
        if (dto.procedimentosIds() == null || dto.procedimentosIds().isEmpty()) {
            throw new RuntimeException("O atendimento deve possuir ao menos 1 procedimento.");
        }

        // Validar número do plano se tipo for PLANO
        if (tipo == AtendimentoTipo.PLANO &&
                (dto.planNumber() == null || dto.planNumber().isBlank())) {
            throw new RuntimeException("Número da carteira do plano é obrigatório em atendimentos de PLANO.");
        }

        Paciente paciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        List<Procedimento> procedimentos =
                procedimentoRepository.findAllById(dto.procedimentosIds());

        if (procedimentos.isEmpty()) {
            throw new RuntimeException("Nenhum procedimento encontrado para os IDs informados.");
        }

        Atendimento atendimento = new Atendimento();
        atendimento.setDateTime(dto.dateTime());
        atendimento.setPaciente(paciente);
        atendimento.setUsuario(usuario);
        atendimento.setProcedimentos(procedimentos);
        atendimento.setTipo(tipo);
        atendimento.setPlanNumber(dto.planNumber());

        // --- cálculo ---
        atendimento.setValorTotal(calcularValor(procedimentos, tipo));

        Atendimento saved = atendimentoRepository.save(atendimento);

        return buildResponse(saved);
    }


    // ------------------------
    // ATUALIZAR
    // ------------------------
    @Transactional
    public AtendimentoResponse atualizar(Long id, AtendimentoDTO dto) {

        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado."));

        AtendimentoTipo tipo = AtendimentoTipo.valueOf(dto.tipo().toUpperCase());

        // Validar procedimentos
        if (dto.procedimentosIds() == null || dto.procedimentosIds().isEmpty()) {
            throw new RuntimeException("O atendimento deve possuir ao menos 1 procedimento.");
        }

        List<Procedimento> procedimentos =
                procedimentoRepository.findAllById(dto.procedimentosIds());

        if (procedimentos.isEmpty()) {
            throw new RuntimeException("Nenhum procedimento encontrado para os IDs informados.");
        }

        // Validar plano
        if (tipo == AtendimentoTipo.PLANO &&
                (dto.planNumber() == null || dto.planNumber().isBlank())) {
            throw new RuntimeException("Número da carteira do plano é obrigatório em atendimentos de PLANO.");
        }

        Paciente paciente = pacienteRepository.findById(dto.pacienteId())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

        Usuario usuario = usuarioRepository.findById(dto.usuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        atendimento.setDateTime(dto.dateTime());
        atendimento.setPaciente(paciente);
        atendimento.setUsuario(usuario);
        atendimento.setProcedimentos(procedimentos);
        atendimento.setTipo(tipo);
        atendimento.setPlanNumber(dto.planNumber());
        atendimento.setValorTotal(calcularValor(procedimentos, tipo));

        Atendimento saved = atendimentoRepository.save(atendimento);
        return buildResponse(saved);
    }

    // ------------------------
    // REMOVER
    // ------------------------
    @Transactional
    public void deletar(Long id) {
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado."));

        atendimentoRepository.delete(atendimento);
    }


    // ------------------------
    // LISTAR E BUSCAR
    // ------------------------
    public AtendimentoResponse buscarPorId(Long id) {
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado."));

        return buildResponse(atendimento);
    }

    public List<AtendimentoResponse> listar() {
        return atendimentoRepository.findAll()
                .stream()
                .map(this::buildResponse)
                .toList();
    }


    // ------------------------
    // MÉTODOS AUXILIARES
    // ------------------------
    private BigDecimal calcularValor(List<Procedimento> procedimentos, AtendimentoTipo tipo) {
        BigDecimal total = BigDecimal.ZERO;

        for (Procedimento p : procedimentos) {
            if (tipo == AtendimentoTipo.PLANO) {
                total = total.add(p.getValorPlan());
            } else {
                total = total.add(p.getValorParticular());
            }
        }

        return total;
    }

    private AtendimentoResponse buildResponse(Atendimento at) {
        return new AtendimentoResponse(
                at.getId(),
                at.getDateTime(),
                at.getPaciente().getPacienteNome(),
                at.getProcedimentos().stream().map(Procedimento::getNome).toList(),
                at.getTipo().name(),
                at.getPlanNumber(),
                at.getUsuario().getNome(),
                at.getValorTotal()
        );
    }
}
