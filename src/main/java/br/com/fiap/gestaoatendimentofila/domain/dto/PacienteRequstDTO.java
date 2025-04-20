package br.com.fiap.gestaoatendimentofila.domain.dto;


public record PacienteRequstDTO(

        String nome,
        String cpf,
        Long unidadeId, // Referência à unidade
        String status, // Ex: AGUARDANDO, ATENDIDO
        String telefone
) {

}

