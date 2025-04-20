package br.com.fiap.gestaoatendimentofila.controller;

import br.com.fiap.gestaoatendimentofila.domain.Paciente;
import br.com.fiap.gestaoatendimentofila.domain.dto.PacienteRequstDTO;
import br.com.fiap.gestaoatendimentofila.service.FilaAtendimentoService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fila")
public class FilaAtendimentoController {
    private final FilaAtendimentoService service;

    public FilaAtendimentoController(FilaAtendimentoService service) {
        this.service = service;
    }

    @PostMapping("/adicionar")
    public Mono<Paciente> adicionar(@RequestBody Paciente paciente) {
        return service.adicionar(paciente);
    }

    @PostMapping("/chamar/{unidadeId}")
    public Mono<Paciente> chamarProximo(@PathVariable Long unidadeId) {
        return service.chamarProximo(unidadeId);
    }

}
