package br.com.fiap.gestaoatendimentofila.domain.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PacienteDTOTest {

    @Test
    void deveCriarPacienteDTOValido() {
        PacienteDTO paciente = new PacienteDTO("João", 1, "11999999999");

        assertEquals("João", paciente.nome());
        assertEquals(1, paciente.posicao());
        assertEquals("11999999999", paciente.numero());
    }

    @Test
    void deveLancarExcecaoQuandoNomeForNulo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new PacienteDTO(null, 1, "11999999999"));

        assertEquals("O nome não pode ser nulo ou vazio.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoNomeForVazio() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new PacienteDTO("   ", 1, "11999999999"));

        assertEquals("O nome não pode ser nulo ou vazio.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoPosicaoNaFilaForMenorOuIgualAZero() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new PacienteDTO("João", 0, "11999999999"));

        assertEquals("A posição na fila não pode ser nula ou vazia.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoTelefoneForNulo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new PacienteDTO("João", 1, null));

        assertEquals("O número não pode ser nulo ou vazio.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoTelefoneForVazio() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new PacienteDTO("João", 1, "   "));

        assertEquals("O número não pode ser nulo ou vazio.", exception.getMessage());
    }
}
