package tech.build.consultas.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.build.consultas.controller.dto.ProcedimentoRequestDTO;
import tech.build.consultas.controller.dto.ProcedimentoResponseDTO;
import tech.build.consultas.entities.Procedimento;
import tech.build.consultas.repositories.AtendimentoRepository;
import tech.build.consultas.repositories.ProcedimentoRepository;

@Service
public class ProcedimentoService {

    private final ProcedimentoRepository repository;
    private final AtendimentoRepository atendimentoRepository;

    public ProcedimentoService(ProcedimentoRepository repository, AtendimentoRepository atendimentoRepository) {
        this.repository = repository;
        this.atendimentoRepository = atendimentoRepository;
    }


    private void validarAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth.getAuthorities().stream()
                .noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {

            throw new RuntimeException("Ação permitida somente para administradores.");
        }
    }

    // Criar
    @Transactional
    public ProcedimentoResponseDTO salvar(ProcedimentoRequestDTO dto) {
        validarAdmin();

        if (repository.existsByNome(dto.nome())) {
            throw new RuntimeException("Já existe um procedimento com esse nome.");
        }

        Procedimento p = new Procedimento();
        copiar(dto, p);

        return ProcedimentoResponseDTO.fromEntity(repository.save(p));
    }

    // Atualizar
    @Transactional
    public ProcedimentoResponseDTO atualizar(Long id, ProcedimentoRequestDTO dto) {
        validarAdmin();

        Procedimento procedimento = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Procedimento não encontrado."));

        // Nome já existe em outro
        repository.findByNome(dto.nome()).ifPresent(existing -> {
            if (!existing.getProcedimentoId().equals(id)) {
                throw new RuntimeException("Já existe outro procedimento com este nome.");
            }
        });

        copiar(dto, procedimento);

        return ProcedimentoResponseDTO.fromEntity(repository.save(procedimento));
    }


    public ProcedimentoResponseDTO buscarPorId(Long id) {
        return repository.findById(id)
                .map(ProcedimentoResponseDTO::fromEntity)
                .orElseThrow(() -> new RuntimeException("Procedimento não encontrado."));
    }

    public Page<ProcedimentoResponseDTO> listar(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ProcedimentoResponseDTO::fromEntity);
    }

    private void copiar(ProcedimentoRequestDTO dto, Procedimento p) {
        p.setNome(dto.nome());
        p.setDescricao(dto.descricao());
        p.setValorPlan(dto.valorPlan());
        p.setValorParticular(dto.valorParticular());
    }

}
