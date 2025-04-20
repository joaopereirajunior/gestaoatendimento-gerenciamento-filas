package br.com.fiap.gestaoatendimentofila.repository;

import br.com.fiap.gestaoatendimentofila.domain.Paciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;

class PacienteRepositoryTest {

    private PacienteRepository repository;

    private Paciente paciente1;
    private Paciente paciente2;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(PacienteRepository.class);

        paciente1 = Paciente.builder()
                .id(1L)
                .nome("Gabriel")
                .cpf("12345678900")
                .status("AGUARDANDO")
                .posicaoNaFila(1)
                .unidadeId(1L)
                .telefone("11999999999")
                .build();

        paciente2 = Paciente.builder()
                .id(2L)
                .nome("João")
                .cpf("98765432100")
                .status("AGUARDANDO")
                .posicaoNaFila(2)
                .unidadeId(1L)
                .telefone("11988888888")
                .build();
    }

    @Test
    void testFindByUnidadeIdAndStatusOrderByIdAsc() {
        Mockito.when(repository.findByUnidadeIdAndStatusOrderByIdAsc(1L, "AGUARDANDO"))
                .thenReturn(Flux.fromIterable(List.of(paciente1, paciente2)));

        StepVerifier.create(repository.findByUnidadeIdAndStatusOrderByIdAsc(1L, "AGUARDANDO"))
                .expectNext(paciente1)
                .expectNext(paciente2)
                .verifyComplete();
    }

    @Test
    void testCountByUnidadeIdAndStatus() {
        Mockito.when(repository.countByUnidadeIdAndStatus(1L, "AGUARDANDO"))
                .thenReturn(Mono.just(2L));

        StepVerifier.create(repository.countByUnidadeIdAndStatus(1L, "AGUARDANDO"))
                .expectNext(2L)
                .verifyComplete();
    }

    @Test
    void testFindFirstByUnidadeIdAndStatusOrderByPosicaoNaFilaAsc() {
        Mockito.when(repository.findFirstByUnidadeIdAndStatusOrderByPosicaoNaFilaAsc(1L, "AGUARDANDO"))
                .thenReturn(Mono.just(paciente1));

        StepVerifier.create(repository.findFirstByUnidadeIdAndStatusOrderByPosicaoNaFilaAsc(1L, "AGUARDANDO"))
                .expectNext(paciente1)
                .verifyComplete();
    }

    @Test
    void testFindByUnidadeIdAndStatusAndPosicaoNaFilaGreaterThan() {
        Mockito.when(repository.findByUnidadeIdAndStatusAndPosicaoNaFilaGreaterThan(1L, "AGUARDANDO", 1))
                .thenReturn(Flux.just(paciente2));

        StepVerifier.create(repository.findByUnidadeIdAndStatusAndPosicaoNaFilaGreaterThan(1L, "AGUARDANDO", 1))
                .expectNext(paciente2)
                .verifyComplete();
    }

    @Test
    void testFindByUnidadeIdAndStatusOrderByPosicaoNaFilaAsc() {
        Mockito.when(repository.findByUnidadeIdAndStatusOrderByPosicaoNaFilaAsc(1L, "AGUARDANDO"))
                .thenReturn(Flux.just(paciente1, paciente2));

        StepVerifier.create(repository.findByUnidadeIdAndStatusOrderByPosicaoNaFilaAsc(1L, "AGUARDANDO"))
                .expectNext(paciente1)
                .expectNext(paciente2)
                .verifyComplete();
    }
}
