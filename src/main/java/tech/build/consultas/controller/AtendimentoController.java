package tech.build.consultas.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.build.consultas.controller.dto.AtendimentoDTO;
import tech.build.consultas.controller.dto.AtendimentoResponse;
import tech.build.consultas.service.AtendimentoService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/atendimentos")
public class AtendimentoController {

    private final AtendimentoService service;

    public AtendimentoController(AtendimentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AtendimentoResponse> criar(@RequestBody AtendimentoDTO dto) {
        AtendimentoResponse response = service.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AtendimentoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody AtendimentoDTO dto
    ) {
        AtendimentoResponse response = service.atualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<AtendimentoResponse> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<AtendimentoResponse>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    // -----------------------------------
    // PAGINAÇÃO
    // -----------------------------------
    @GetMapping("/page")
    public ResponseEntity<Page<AtendimentoResponse>> listarPaginado(Pageable pageable) {
        return ResponseEntity.ok(service.listarPaginado(pageable));
    }

    // -----------------------------------
    // FILTRO ENTRE DATAS
    // -----------------------------------
    @GetMapping("/buscar-entre-datas")
    public ResponseEntity<Page<AtendimentoResponse>> buscarEntreDatas(
            @RequestParam LocalDateTime inicio,
            @RequestParam LocalDateTime fim,
            Pageable pageable
    ) {
        return ResponseEntity.ok(service.listarEntreDatas(inicio, fim, pageable));
    }
}

