package tech.build.consultas.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.build.consultas.controller.dto.PacienteDTO;
import tech.build.consultas.controller.dto.PacienteResponse;
import tech.build.consultas.controller.dto.PacienteResponseDTO;
import tech.build.consultas.controller.dto.PacienteUpdateDTO;
import tech.build.consultas.entities.Paciente;
import tech.build.consultas.service.PacienteService;

import java.net.URI;


@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    // Criar paciente
    @PostMapping
    public ResponseEntity<PacienteResponse> criarPaciente(@RequestBody PacienteDTO pacienteDTO) {
        PacienteResponse novoPaciente = pacienteService.salvarPaciente(pacienteDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPaciente);
    }

    // Atualizar paciente
    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponse> atualizarPaciente(
            @PathVariable Long id,
            @RequestBody PacienteDTO dto
    ) {
        PacienteResponse atualizado = pacienteService.atualizar(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    // Remover paciente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPaciente(@PathVariable Long id) {
        pacienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    // Listar pacientes com paginação
    @GetMapping
    public ResponseEntity<Page<PacienteResponseDTO>> listarPacientes(
            @PageableDefault(size = 10, sort = "pacienteId") Pageable pageable
    ) {
        Page<PacienteResponseDTO> pagina = pacienteService.listar(pageable);
        return ResponseEntity.ok(pagina);
    }
}
