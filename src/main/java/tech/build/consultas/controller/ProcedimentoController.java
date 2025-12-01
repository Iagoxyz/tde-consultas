package tech.build.consultas.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.build.consultas.controller.dto.ProcedimentoRequestDTO;
import tech.build.consultas.controller.dto.ProcedimentoResponseDTO;
import tech.build.consultas.service.ProcedimentoService;

@RestController
@RequestMapping("/api/procedimentos")
public class ProcedimentoController {

    private final ProcedimentoService service;

    public ProcedimentoController(ProcedimentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProcedimentoResponseDTO> criar(
            @RequestBody ProcedimentoRequestDTO dto
    ) {
        ProcedimentoResponseDTO response = service.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcedimentoResponseDTO> atualizar(
            @PathVariable Long id,
            @RequestBody ProcedimentoRequestDTO dto
    ) {
        ProcedimentoResponseDTO response = service.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcedimentoResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<Page<ProcedimentoResponseDTO>> listar(
            @PageableDefault(size = 10) Pageable pageable
    ) {
        return ResponseEntity.ok(service.listar(pageable));
    }
}