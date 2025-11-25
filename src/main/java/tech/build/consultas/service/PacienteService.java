package tech.build.consultas.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.build.consultas.controller.dto.PacienteDTO;
import tech.build.consultas.controller.dto.PacienteResponse;
import tech.build.consultas.controller.dto.PacienteResponseDTO;
import tech.build.consultas.controller.dto.PacienteUpdateDTO;
import tech.build.consultas.entities.Endereco;
import tech.build.consultas.entities.Paciente;
import tech.build.consultas.entities.Responsavel;
import tech.build.consultas.repositories.PacienteRepository;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional
    public PacienteResponse salvarPaciente(PacienteDTO dto) {
        Paciente paciente = new Paciente();

        paciente.setPacienteCpf(dto.pacienteCpf());
        paciente.setPacienteNome(dto.pacienteNome());
        paciente.setPacienteEmail(dto.pacienteEmail());
        paciente.setPacienteTelefone(dto.pacienteTelefone());
        paciente.setDataNascimento(dto.dataNascimento());

        if (dto.endereco() != null) {
            Endereco endereco = new Endereco();
            endereco.setEstado(dto.endereco().estado());
            endereco.setCidade(dto.endereco().cidade());
            endereco.setBairro(dto.endereco().bairro());
            endereco.setCep(dto.endereco().cep());
            endereco.setRua(dto.endereco().rua());
            endereco.setNumero(dto.endereco().numero());
            paciente.setEndereco(endereco);
        }

        if (dto.responsavel() != null) {
            Responsavel responsavel = new Responsavel();
            responsavel.setCpf(dto.responsavel().cpf());
            responsavel.setNome(dto.responsavel().nome());
            responsavel.setDateOfBirth(dto.responsavel().dataNascimento());
            responsavel.setEmail(dto.responsavel().email());
            responsavel.setTelefone(dto.responsavel().telefone());
            paciente.setResponsavel(responsavel);
        }

        Paciente salvo = pacienteRepository.save(paciente);

        // 🔹 Monta o DTO de resposta
        PacienteResponse.ResponsavelResponse respDTO = null;
        if (salvo.getResponsavel() != null) {
            respDTO = new PacienteResponse.ResponsavelResponse(
                    salvo.getResponsavel().getNome(),
                    salvo.getResponsavel().getEmail(),
                    salvo.getResponsavel().getTelefone()
            );
        }

        return new PacienteResponse(
                salvo.getPacienteNome(),
                salvo.getPacienteEmail(),
                salvo.getPacienteTelefone(),
                salvo.getDataNascimento(),
                respDTO
        );
    }

    @Transactional(readOnly = true)
    public PacienteResponseDTO buscarPorId(Long id) {
        return pacienteRepository.findById(id)
                .map(paciente -> new PacienteResponseDTO(
                        paciente.getPacienteId(),
                        paciente.getPacienteNome(),
                        paciente.getPacienteEmail(),
                        paciente.getPacienteTelefone(),
                        paciente.getDataNascimento()
                ))
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com ID: " + id));
    }

    @Transactional
    public void deletar(Long id) {
        if (!pacienteRepository.existsById(id)) {
            throw new RuntimeException("Paciente não encontrado com ID: " + id);
        }
        pacienteRepository.deleteById(id);
    }

    @Transactional
    public PacienteResponseDTO atualizar(Long id, PacienteUpdateDTO dto) {
        var paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado com ID: " + id));

        // Atualiza os campos permitidos
        paciente.setPacienteNome(dto.nome());
        paciente.setPacienteEmail(dto.email());
        paciente.setPacienteTelefone(dto.telefone());
        paciente.setDataNascimento(dto.dataNascimento());

        // Salva as alterações
        Paciente atualizado = pacienteRepository.save(paciente);

        return new PacienteResponseDTO(
                atualizado.getPacienteId(),
                atualizado.getPacienteNome(),
                atualizado.getPacienteEmail(),
                atualizado.getPacienteTelefone(),
                atualizado.getDataNascimento()
        );
    }


}