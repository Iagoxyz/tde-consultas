package tech.build.consultas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.build.consultas.controller.dto.AtendimentoDTO;
import tech.build.consultas.controller.dto.AtendimentoResponse;
import tech.build.consultas.service.AtendimentoService;

@RestController
@RequestMapping("/api/atendimentos")
public class AtendimentoController {

    private final AtendimentoService atendimentoService;

    public AtendimentoController(AtendimentoService atendimentoService) {
        this.atendimentoService = atendimentoService;
    }

    @PostMapping
    public ResponseEntity<AtendimentoResponse> criar(@RequestBody AtendimentoDTO dto) {
        AtendimentoResponse response = atendimentoService.criar(dto);
        return ResponseEntity.ok(response);
    }
}
