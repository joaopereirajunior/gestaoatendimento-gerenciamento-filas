-- init.sql
CREATE TABLE IF NOT EXISTS pacientes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    telefone VARCHAR(20),
    unidade_id BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    posicao_na_fila INT NOT NULL
);