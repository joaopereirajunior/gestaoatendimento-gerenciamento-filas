#  Sistema de Gerenciamento de Filas

Este projeto faz parte do *Tech Challenge - Hackaton Fase 5*, nele foi construido um Sistema de Gerenciamento de Filas com sua construção baseada na estrutura *MVC*, utilizando tecnologias modernas como *Java*, *Spring Boot*, *Spring WebFlux* e *Docker*, com foco na usabilidade e na escalabilidade. O sistema permite o gerenciamento de filas.

## Tecnologias Utilizadas

- **Java 17**: Versão de linguagem utilizada.
- **Spring Boot**: Framework para desenvolvimento de aplicações Java.
- **Spring WebFlux**: Framework reativo para criação de APIs não bloqueantes e escaláveis. 
- **Swagger**: Para documentação e testes das APIs.
- **JUnit, AssertJ**: Para criação de testes unitários e integrados.

## Instruções para Acesso à Aplicação

A aplicação se encontra disponível no seguinte endereço:

URL LOCAL: [[http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)]

## Para executar a aplicação via Docker, siga os comandos abaixo:

1. **Faça login no Docker:**
   ```bash
   docker login
    ```
2. **Execute o seguinte comando para subir o serviço em um ambiente Docker:**
     ```bash
    docker compose up -d
    ```
## Instruções para Execução dos Testes

- Comando para execução dos **Testes Unitários**:
   ```bash
    mvn test
    ```

## Documentação da API

A documentação da API é gerada automaticamente pelo Swagger. Você pode acessá-la inserindo /swagger-ui/index.html ao final da url ou no seguinte endereço após iniciar a aplicação localmente:

URL LOCAL: [[http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)]

Consulte a documentação do Swagger UI para ver todos os endpoints disponíveis e detalhes sobre cada um deles.

## Banco de Dados

A aplicação se conecta a um banco de dados MariaDB, um banco de dados relacional, utilizado para armazenar e organizar todas os dados da aplicação.
