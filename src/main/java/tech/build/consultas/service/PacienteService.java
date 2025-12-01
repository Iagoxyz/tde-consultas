package tech.build.consultas.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.build.consultas.controller.dto.*;
import tech.build.consultas.entities.Endereco;
import tech.build.consultas.entities.Paciente;
import tech.build.consultas.entities.Responsavel;
import tech.build.consultas.repositories.PacienteRepository;

import java.time.LocalDate;
import java.time.Period;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional
    public PacienteResponse salvarPaciente(PacienteDTO dto) {

        validarPaciente(dto, null);

        Paciente paciente = new Paciente();
        copiarDtoParaEntidade(dto, paciente);

        if (dto.responsavel() != null) {
            Responsavel resp = criarResponsavelEmbeddable(dto.responsavel());
            paciente.setResponsavel(resp);
        }

        Paciente salvo = pacienteRepository.save(paciente);
        return PacienteResponse.fromEntity(salvo);
    }

    @Transactional
    public PacienteResponse atualizar(Long id, PacienteDTO dto) {

        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));

        validarPaciente(dto, id);

        copiarDtoParaEntidade(dto, paciente);

        // Atualizar responsável:
        if (dto.responsavel() != null) {
            Responsavel responsavel = criarResponsavelEmbeddable(dto.responsavel());
            paciente.setResponsavel(responsavel);
        } else {
            // Paciente maior pode remover responsável
            int idade = Period.between(paciente.getDataNascimento(), LocalDate.now()).getYears();
            if (idade < 18) {
                throw new RuntimeException("Paciente menor de idade não pode remover o responsável.");
            }
            paciente.setResponsavel(null);
        }

        Paciente atualizado = pacienteRepository.save(paciente);
        return PacienteResponse.fromEntity(atualizado);
    }


    @Transactional
    public void deletar(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));


        pacienteRepository.delete(paciente);
    }

    public Page<PacienteResponseDTO> listar(Pageable pageable) {
        return pacienteRepository.findAll(pageable)
                .map(PacienteResponseDTO::fromEntity);
    }

    private void validarPaciente(PacienteDTO dto, Long ignoreId) {

        pacienteRepository.findByPacienteCpf(dto.pacienteCpf()).ifPresent(p -> {
            if (!p.getPacienteId().equals(ignoreId)) {
                throw new RuntimeException("Já existe paciente com este CPF.");
            }
        });

        pacienteRepository.findByPacienteEmail(dto.pacienteEmail()).ifPresent(p -> {
            if (!p.getPacienteId().equals(ignoreId)) {
                throw new RuntimeException("Já existe paciente com este e-mail.");
            }
        });

        int idadePaciente = Period.between(dto.dataNascimento(), LocalDate.now()).getYears();
        if (idadePaciente < 18 && dto.responsavel() == null) {
            throw new RuntimeException("Paciente menor de idade precisa de um responsável.");
        }

        if (dto.responsavel() != null) {
            int idadeResp = Period.between(dto.responsavel().dataNascimento(), LocalDate.now()).getYears();
            if (idadeResp < 18) {
                throw new RuntimeException("O responsável deve ter no mínimo 18 anos.");
            }
        }
    }

    private void copiarDtoParaEntidade(PacienteDTO dto, Paciente paciente) {
        paciente.setPacienteNome(dto.pacienteNome());
        paciente.setPacienteCpf(dto.pacienteCpf());
        paciente.setPacienteEmail(dto.pacienteEmail());
        paciente.setPacienteTelefone(dto.pacienteTelefone());
        paciente.setDataNascimento(dto.dataNascimento());

        if (dto.endereco() != null) {
            Endereco e = new Endereco();
            e.setEstado(dto.endereco().estado());
            e.setCidade(dto.endereco().cidade());
            e.setBairro(dto.endereco().bairro());
            e.setCep(dto.endereco().cep());
            e.setRua(dto.endereco().rua());
            e.setNumero(dto.endereco().numero());
            paciente.setEndereco(e);
        }
    }

    private Responsavel criarResponsavelEmbeddable(ResponsavelDTO dto) {
        Responsavel r = new Responsavel();
        r.setCpf(dto.cpf());
        r.setNome(dto.nome());
        r.setDateOfBirth(dto.dataNascimento());
        r.setEmail(dto.email());
        r.setTelefone(dto.telefone());
        return r;
    }

    @Transactional(readOnly = true)
    public PacienteResponse buscarPorId(Long id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado."));
        return PacienteResponse.fromEntity(paciente);
    }
}
