package br.com.fiap.gestaoatendimentofila.domain.dto;

public record PacienteDTO(
        String nome,
        int posicao,
        String numero) {
    public PacienteDTO {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome não pode ser nulo ou vazio.");
        }
        if (posicao <=0 ) {
            throw new IllegalArgumentException("A posição na fila não pode ser nula ou vazia.");
        }
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("O número não pode ser nulo ou vazio.");
        }
    }

}
