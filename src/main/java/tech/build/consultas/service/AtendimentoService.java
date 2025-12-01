package tech.build.consultas.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.build.consultas.controller.dto.AtendimentoDTO;
import tech.build.consultas.controller.dto.AtendimentoResponse;
import tech.build.consultas.entities.*;
import tech.build.consultas.repositories.AtendimentoRepository;
import tech.build.consultas.repositories.PacienteRepository;
import tech.build.consultas.repositories.ProcedimentoRepository;
import tech.build.consultas.repositories.UsuarioRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    // -----------------------------------
    // CRIAR
    // -----------------------------------
    @Transactional
    public AtendimentoResponse criar(AtendimentoDTO dto) {
        AtendimentoTipo tipo = AtendimentoTipo.valueOf(dto.tipo().toUpperCase());

        if (dto.procedimentosIds() == null || dto.procedimentosIds().isEmpty()) {
            throw new RuntimeException("O atendimento deve possuir ao menos 1 procedimento.");
        }

        if (tipo == AtendimentoTipo.PLANO &&
                (dto.planNumber() == null || dto.planNumber().isBlank())) {
            throw new RuntimeException("Número da carteira do plano é obrigatório.");
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
        atendimento.setValorTotal(calcularValor(procedimentos, tipo));

        Atendimento saved = atendimentoRepository.save(atendimento);

        return buildResponse(saved);
    }

    // -----------------------------------
    // ATUALIZAR
    // -----------------------------------
    @Transactional
    public AtendimentoResponse atualizar(Long id, AtendimentoDTO dto) {

        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado."));

        AtendimentoTipo tipo = AtendimentoTipo.valueOf(dto.tipo().toUpperCase());

        if (dto.procedimentosIds() == null || dto.procedimentosIds().isEmpty()) {
            throw new RuntimeException("O atendimento deve possuir ao menos 1 procedimento.");
        }

        List<Procedimento> procedimentos =
                procedimentoRepository.findAllById(dto.procedimentosIds());

        if (procedimentos.isEmpty()) {
            throw new RuntimeException("Nenhum procedimento encontrado.");
        }

        if (tipo == AtendimentoTipo.PLANO &&
                (dto.planNumber() == null || dto.planNumber().isBlank())) {
            throw new RuntimeException("Número da carteira do plano é obrigatório.");
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

    // -----------------------------------
    // REMOVER
    // -----------------------------------
    @Transactional
    public void deletar(Long id) {
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado."));

        atendimentoRepository.delete(atendimento);
    }

    // -----------------------------------
    // BUSCAR POR ID
    // -----------------------------------
    public AtendimentoResponse buscarPorId(Long id) {
        Atendimento atendimento = atendimentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Atendimento não encontrado."));

        return buildResponse(atendimento);
    }

    // -----------------------------------
    // LISTAR TUDO (SEM PAGINAÇÃO)
    // -----------------------------------
    public List<AtendimentoResponse> listar() {
        return atendimentoRepository.findAll()
                .stream()
                .map(this::buildResponse)
                .toList();
    }

    // -----------------------------------
    // LISTAR PAGINADO
    // -----------------------------------
    public Page<AtendimentoResponse> listarPaginado(Pageable pageable) {
        return atendimentoRepository.findAll(pageable)
                .map(this::buildResponse);
    }

    // -----------------------------------
    // LISTAR ENTRE DATAS PAGINADO
    // -----------------------------------
    public Page<AtendimentoResponse> listarEntreDatas(
            LocalDateTime inicio,
            LocalDateTime fim,
            Pageable pageable
    ) {
        Page<Atendimento> page = atendimentoRepository.buscarEntreDatas(inicio, fim, pageable);
        return page.map(this::buildResponse);
    }

    // -----------------------------------
    // MÉTODOS AUXILIARES
    // -----------------------------------
    private BigDecimal calcularValor(List<Procedimento> procedimentos, AtendimentoTipo tipo) {
        BigDecimal total = BigDecimal.ZERO;

        for (Procedimento p : procedimentos) {
            total = total.add(tipo == AtendimentoTipo.PLANO
                    ? p.getValorPlan()
                    : p.getValorParticular());
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
