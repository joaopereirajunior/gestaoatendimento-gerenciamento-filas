package br.com.fiap.gestaoatendimentofila.controller;

import br.com.fiap.gestaoatendimentofila.domain.Paciente;
import br.com.fiap.gestaoatendimentofila.service.FilaAtendimentoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class FilaAtendimentoControllerTest {

    private FilaAtendimentoService service;
    private FilaAtendimentoController controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(FilaAtendimentoService.class);
        controller = new FilaAtendimentoController(service);
    }

    @Test
    void testAdicionarPaciente() {
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNome("João");

        Mockito.when(service.adicionar(any(Paciente.class))).thenReturn(Mono.just(paciente));

        Mono<Paciente> result = controller.adicionar(paciente);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getId().equals(1L) && p.getNome().equals("João"))
                .verifyComplete();

        Mockito.verify(service).adicionar(paciente);
    }

    @Test
    void testChamarProximo() {
        Long unidadeId = 123L;
        Paciente paciente = new Paciente();
        paciente.setId(2L);
        paciente.setNome("Maria");

        Mockito.when(service.chamarProximo(eq(unidadeId))).thenReturn(Mono.just(paciente));

        Mono<Paciente> result = controller.chamarProximo(unidadeId);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getId().equals(2L) && p.getNome().equals("Maria"))
                .verifyComplete();

        Mockito.verify(service).chamarProximo(unidadeId);
    }
}
