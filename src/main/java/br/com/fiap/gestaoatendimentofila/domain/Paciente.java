package br.com.fiap.gestaoatendimentofila.domain;

import br.com.fiap.gestaoatendimentofila.domain.dto.PacienteRequstDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("pacientes")
public class Paciente implements Serializable {

    @Id
    private Long id;
    private String nome;
    private String cpf;
    private Long unidadeId; // Referência à unidade
    private String status;  // Ex: AGUARDANDO, ATENDIDO
    private String telefone;
    @Column("posicao_na_fila")
    private int posicaoNaFila;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Long getUnidadeId() {
        return unidadeId;
    }

    public void setUnidadeId(Long unidadeId) {
        this.unidadeId = unidadeId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTelefone()
    {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public int getPosicaoNaFila() {
        return posicaoNaFila;
    }

    public void setPosicaoNaFila(int posicaoNaFila) {
        this.posicaoNaFila = posicaoNaFila;
    }

    public  static Paciente fromDTO(PacienteRequstDTO pacienteRequstDTO) {
        Paciente paciente = new Paciente();
        paciente.setNome(pacienteRequstDTO.nome());
        paciente.setCpf(pacienteRequstDTO.cpf());
        paciente.setUnidadeId(pacienteRequstDTO.unidadeId());
        paciente.setStatus(pacienteRequstDTO.status());
        paciente.setTelefone(pacienteRequstDTO.telefone());
        return paciente;
    }
}
