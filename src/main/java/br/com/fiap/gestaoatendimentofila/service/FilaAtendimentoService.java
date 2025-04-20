package br.com.fiap.gestaoatendimentofila.service;

import br.com.fiap.gestaoatendimentofila.domain.Paciente;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface FilaAtendimentoService {

    public Mono<Paciente> adicionar(Paciente paciente);
    public Mono<Paciente> chamarProximo(Long unidadeId);

}

