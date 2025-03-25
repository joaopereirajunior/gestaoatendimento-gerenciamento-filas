package br.com.fiap.gestaoatendimentofila.service;

import br.com.fiap.gestaoatendimentofila.config.rabbit.RabbitMQConfig;
import br.com.fiap.gestaoatendimentofila.domain.Paciente;
import br.com.fiap.gestaoatendimentofila.repository.PacienteRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FilaAtendimentoService {
    private final RabbitTemplate rabbitTemplate;
    private final PacienteRepository pacienteRepository;
    private NotificacaoService notificacaoService;


    public FilaAtendimentoService(RabbitTemplate rabbitTemplate, PacienteRepository pacienteRepository, NotificacaoService notificacaoService)
    {
        this.rabbitTemplate = rabbitTemplate;
        this.pacienteRepository = pacienteRepository;
        this.notificacaoService = notificacaoService;
    }

    /*public Mono<Paciente> adicionar(Paciente paciente) {
        paciente.setStatus("AGUARDANDO");
        rabbitTemplate.convertAndSend(RabbitMQConfig.FILA_ATENDIMENTO, pacienteF);

        // Enviar notificação
        String mensagem = "Olá " + paciente.getNome() + ", você entrou na fila de atendimento.";
        notificacaoService.enviarSms(paciente.getTelefone(), mensagem);

        return pacienteRepository.save(paciente);

    }*/

    public Mono<Paciente> adicionar(Paciente paciente) {
        paciente.setStatus("AGUARDANDO");

        return pacienteRepository
                .countByUnidadeIdAndStatus(paciente.getUnidadeId(), "AGUARDANDO")
                .map(posicaoAtual -> {
                    paciente.setPosicaoNaFila(posicaoAtual.intValue() + 1); // próxima posição na unidade
                    return paciente;
                })
                .flatMap(pacienteFinal -> {
                    // Enviar para fila RabbitMQ
                    rabbitTemplate.convertAndSend(RabbitMQConfig.FILA_ATENDIMENTO, pacienteFinal);

                    // Enviar SMS de notificação
                    String mensagem = "Olá " + pacienteFinal.getNome() +
                            ", você entrou na fila da unidade " + pacienteFinal.getUnidadeId() +
                            " na posição número " + pacienteFinal.getPosicaoNaFila() + ".";
                    notificacaoService.enviarSms(pacienteFinal.getTelefone(), mensagem);

                    // Salvar no banco
                    return pacienteRepository.save(pacienteFinal);
                });
    }


    /*public Mono<Paciente> chamarProximo(Long unidadeId) {
        return pacienteRepository.findByUnidadeIdAndStatusOrderByIdAsc(unidadeId, "AGUARDANDO")
                .next()

                .flatMap(paciente -> {
                    paciente.setStatus("ATENDIDO");
                    return pacienteRepository.save(paciente);
                });
    }*/
    public Mono<Paciente> chamarProximo(Long unidadeId) {
        return pacienteRepository
                .findFirstByUnidadeIdAndStatusOrderByPosicaoNaFilaAsc(unidadeId, "AGUARDANDO")
                .flatMap(primeiro -> {
                    primeiro.setStatus("ATENDIDO");

                    return pacienteRepository.save(primeiro)
                            .thenMany(
                                    pacienteRepository.findByUnidadeIdAndStatusAndPosicaoNaFilaGreaterThan(
                                            unidadeId, "AGUARDANDO", primeiro.getPosicaoNaFila()
                                    )
                            )
                            .flatMap(paciente -> {
                                paciente.setPosicaoNaFila(paciente.getPosicaoNaFila() - 1);
                                return pacienteRepository.save(paciente);
                            })
                            .then(Mono.just(primeiro));
                });
    }

}
