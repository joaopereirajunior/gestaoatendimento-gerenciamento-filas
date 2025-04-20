package br.com.fiap.gestaoatendimentofila.service.impl;

import br.com.fiap.gestaoatendimentofila.domain.Paciente;
import br.com.fiap.gestaoatendimentofila.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class FilaAtendimentoServiceImplTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private WebClient.RequestBodySpec requestBodySpec;

    @Mock
    private WebClient.RequestHeadersSpec<?> requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private FilaAtendimentoServiceImpl filaAtendimentoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock WebClient fluente
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenAnswer(invocation -> requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        // Injetar WebClient mockado na service
        filaAtendimentoService = new FilaAtendimentoServiceImpl(pacienteRepository) {
            @Override
            public Mono<Paciente> adicionar(Paciente paciente) {
                paciente.setStatus("AGUARDANDO");
                return pacienteRepository
                        .countByUnidadeIdAndStatus(paciente.getUnidadeId(), "AGUARDANDO")
                        .map(posicaoAtual -> {
                            paciente.setPosicaoNaFila(posicaoAtual.intValue() + 1);
                            return paciente;
                        })
                        .flatMap(pacienteFinal -> pacienteRepository.save(pacienteFinal)
                                .flatMap(saved -> webClient.post()
                                        .uri("http://localhost:9092/api/notificacoes/entrada")
                                        .bodyValue(saved)
                                        .retrieve()
                                        .bodyToMono(Void.class)
                                        .thenReturn(saved)));
            }

            @Override
            public Mono<Paciente> chamarProximo(Long unidadeId) {
                return pacienteRepository
                        .findFirstByUnidadeIdAndStatusOrderByPosicaoNaFilaAsc(unidadeId, "AGUARDANDO")
                        .flatMap(paciente -> {
                            paciente.setStatus("ATENDIDO");
                            return pacienteRepository.save(paciente)
                                    .flatMap(saved -> webClient.post()
                                            .uri("http://localhost:9092/api/notificacoes/chamada")
                                            .bodyValue(saved)
                                            .retrieve()
                                            .bodyToMono(Void.class)
                                            .thenReturn(saved));
                        });
            }
        };
    }

    @Test
    void testAdicionarPaciente() {
        Paciente paciente = Paciente.builder()
                .id(1L)
                .nome("Gabriel")
                .cpf("12345678900")
                .telefone("11999999999")
                .unidadeId(1L)
                .status("AGUARDANDO")
                .posicaoNaFila(0)
                .build();

        when(pacienteRepository.countByUnidadeIdAndStatus(anyLong(), eq("AGUARDANDO")))
                .thenReturn(Mono.just(0L));

        when(pacienteRepository.save(any(Paciente.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Paciente> result = filaAtendimentoService.adicionar(paciente);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getNome().equals("Gabriel") && p.getPosicaoNaFila() == 1)
                .verifyComplete();

        verify(pacienteRepository).save(any(Paciente.class));
        verify(webClient).post();
    }

    @Test
    void testChamarProximo() {
        Long unidadeId = 1L;

        Paciente paciente = Paciente.builder()
                .id(1L)
                .nome("Gabriel")
                .cpf("12345678900")
                .telefone("11999999999")
                .unidadeId(unidadeId)
                .status("AGUARDANDO")
                .posicaoNaFila(1)
                .build();

        when(pacienteRepository.findFirstByUnidadeIdAndStatusOrderByPosicaoNaFilaAsc(unidadeId, "AGUARDANDO"))
                .thenReturn(Mono.just(paciente));

        when(pacienteRepository.save(any(Paciente.class)))
                .thenAnswer(invocation -> Mono.just(invocation.getArgument(0)));

        Mono<Paciente> result = filaAtendimentoService.chamarProximo(unidadeId);

        StepVerifier.create(result)
                .expectNextMatches(p -> p.getStatus().equals("ATENDIDO") && p.getNome().equals("Gabriel"))
                .verifyComplete();

        verify(pacienteRepository).save(any(Paciente.class));
        verify(webClient).post();
    }
}
