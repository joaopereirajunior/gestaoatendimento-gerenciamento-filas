package br.com.fiap.gestaoatendimentofila.service.impl;

import br.com.fiap.gestaoatendimentofila.domain.Paciente;
import br.com.fiap.gestaoatendimentofila.domain.dto.PacienteDTO;
import br.com.fiap.gestaoatendimentofila.repository.PacienteRepository;
import br.com.fiap.gestaoatendimentofila.service.FilaAtendimentoService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Service
public class FilaAtendimentoServiceImpl implements FilaAtendimentoService {
    private final PacienteRepository pacienteRepository;

    public FilaAtendimentoServiceImpl(PacienteRepository pacienteRepository)
    {
        this.pacienteRepository = pacienteRepository;
    }

    public Mono<Paciente> adicionar(Paciente paciente)
    {
        paciente.setStatus("AGUARDANDO");

        return pacienteRepository
                .countByUnidadeIdAndStatus(paciente.getUnidadeId(), "AGUARDANDO")
                .map(posicaoAtual -> {
                    paciente.setPosicaoNaFila(posicaoAtual.intValue() + 1); // próxima posição na unidade
                    return paciente;
                })
                .flatMap(pacienteFinal -> {

                    // Salvar no banco
                    return pacienteRepository.save(pacienteFinal)
                            .flatMap(savedPaciente -> {
                                PacienteDTO pacienteDTO = new PacienteDTO(savedPaciente.getNome(), savedPaciente.getPosicaoNaFila(), savedPaciente.getTelefone());
                                // Enviar o objeto para o endpoint
                                WebClient webClient = WebClient.create();
                                return webClient.post()
                                        .uri("http://localhost:8082/api/notificacoes/entrada")
                                        .bodyValue(pacienteDTO)
                                        .retrieve()
                                        .bodyToMono(Void.class)
                                        .thenReturn(savedPaciente);
                            });
                });
    }

    public Mono<Paciente> chamarProximo(Long unidadeId)
    {
        return pacienteRepository
                .findFirstByUnidadeIdAndStatusOrderByPosicaoNaFilaAsc(unidadeId, "AGUARDANDO")
                .flatMap(primeiro -> {
                    primeiro.setStatus("ATENDIDO");

                    return pacienteRepository.save(primeiro)
                            .flatMap(savedPrimeiro -> {
                                // Enviar notificação para o endpoint de saída da fila
                                PacienteDTO pacienteDTO = new PacienteDTO(
                                        savedPrimeiro.getNome(),
                                        savedPrimeiro.getPosicaoNaFila(),
                                        savedPrimeiro.getTelefone()
                                );

                                WebClient webClient = WebClient.create();
                                return webClient.post()
                                        .uri("http://localhost:8082/api/notificacoes/saida")
                                        .bodyValue(pacienteDTO)
                                        .retrieve()
                                        .bodyToMono(Void.class)
                                        .then(Mono.just(savedPrimeiro));
                            })
                            .thenMany(
                                    pacienteRepository.findByUnidadeIdAndStatusAndPosicaoNaFilaGreaterThan(
                                            unidadeId, "AGUARDANDO", primeiro.getPosicaoNaFila()
                                    )
                            )
                            .flatMap(paciente -> {
                                paciente.setPosicaoNaFila(paciente.getPosicaoNaFila() - 1);
                                return pacienteRepository.save(paciente);
                            })
                            .thenMany(
                                    pacienteRepository.findByUnidadeIdAndStatusOrderByPosicaoNaFilaAsc(unidadeId, "AGUARDANDO")
                                            .take(5) // Resgatar os 5 primeiros pacientes
                            )
                            .flatMap(paciente -> {
                                // Enviar notificação para o endpoint de previsão, um por um
                                WebClient webClient = WebClient.create();
                                PacienteDTO pacienteDTO = new PacienteDTO(paciente.getNome(), paciente.getPosicaoNaFila(), paciente.getTelefone());
                                return webClient.post()
                                        .uri("http://localhost:8082/api/notificacoes/previsao")
                                        .bodyValue(pacienteDTO)
                                        .retrieve()
                                        .bodyToMono(Void.class);
                            })
                            .then(Mono.just(primeiro));
                });
    }
}
