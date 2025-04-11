package br.com.fiap.gestaoatendimentofila.repository;

import br.com.fiap.gestaoatendimentofila.domain.Paciente;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PacienteRepository extends ReactiveCrudRepository<Paciente, Long> {
    Flux<Paciente> findByUnidadeIdAndStatusOrderByIdAsc(Long unidadeId, String status);
    Mono<Long> countByUnidadeIdAndStatus(Long unidadeId, String status);

    Mono<Paciente> findFirstByUnidadeIdAndStatusOrderByPosicaoNaFilaAsc(Long unidadeId, String status);

    Flux<Paciente> findByUnidadeIdAndStatusAndPosicaoNaFilaGreaterThan(Long unidadeId, String status, int posicao);
    Flux<Paciente> findByUnidadeIdAndStatusOrderByPosicaoNaFilaAsc(Long unidadeId, String status);
}
