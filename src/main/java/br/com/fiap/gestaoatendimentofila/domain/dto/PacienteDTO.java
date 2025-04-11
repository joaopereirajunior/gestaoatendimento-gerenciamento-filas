package br.com.fiap.gestaoatendimentofila.domain.dto;

public record PacienteDTO(
        String nome,
        int posicaoNaFila,
        String telefone) {
    public PacienteDTO {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome não pode ser nulo ou vazio.");
        }
        if (posicaoNaFila <=0 ) {
            throw new IllegalArgumentException("A posição na fila não pode ser nula ou vazia.");
        }
        if (telefone == null || telefone.isBlank()) {
            throw new IllegalArgumentException("O número não pode ser nulo ou vazio.");
        }
    }

}
