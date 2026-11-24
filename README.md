# Bank Simulator

![Java](https://img.shields.io/badge/Java-Backend-000000?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Framework-000000?style=for-the-badge&logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build%20Tool-000000?style=for-the-badge&logo=apachemaven&logoColor=white)
![JPA](https://img.shields.io/badge/Spring%20Data%20JPA-Persistence-000000?style=for-the-badge&logoColor=white)
![Status](https://img.shields.io/badge/Status-Em%20desenvolvimento-000000?style=for-the-badge)

## Sobre o projeto

O **Bank Simulator** é uma aplicação de simulação bancária desenvolvida em Java, com foco na aplicação de conceitos de Programação Orientada a Objetos, arquitetura de software e desenvolvimento backend.

O projeto iniciou como uma aplicação Java puro e evoluiu para uma arquitetura baseada em **Spring Boot e Maven**, incorporando desenvolvimento de APIs REST, organização em camadas, persistência com JPA/Hibernate, tratamento padronizado de erros e testes automatizados.

## Evolução do projeto

O desenvolvimento foi dividido em duas etapas principais:

**Versão 1 — Java puro**
- Estrutura baseada em Programação Orientada a Objetos.
- Modelagem de entidades bancárias.
- Implementação de regras de negócio no console.

**Versão 2 — Spring Boot e Maven**
- Migração para arquitetura backend em camadas.
- Exposição de endpoints REST e validação com Bean Validation (`@Valid`).
- Gerenciamento de dependências com Maven.
- Persistência com Spring Data JPA, Hibernate e banco H2.
- Controle de concorrência otimista (`@Version`) em contas.
- Tratamento centralizado de exceções com `ProblemDetail`.
- Testes unitários de domínio e testes de integração com `MockMvc`.

## Tecnologias utilizadas

| Tecnologia | Finalidade |
|---|---|
| Java 17+ | Linguagem de programação |
| Spring Boot | Framework para desenvolvimento backend |
| Apache Maven | Gerenciamento de dependências e build |
| Spring Data JPA | Persistência e acesso a dados |
| Hibernate | Mapeamento objeto-relacional (ORM) |
| H2 Database | Banco de dados para desenvolvimento e testes |
| Bean Validation | Validação de dados de entrada nos DTOs |
| Swagger / Springdoc | Documentação interativa da API |
| JUnit 5 & MockMvc | Testes unitários e de integração |

## Arquitetura do projeto

A aplicação utiliza uma arquitetura organizada em camadas dentro de `com.bancojava.simulador`:

| Pacote | Responsabilidade |
|---|---|
| `config` | Configurações e carga inicial de demonstração (`DataInitializer`) |
| `controller` | Recebimento de requisições HTTP e tratamento global de erros (`GlobalExceptionHandler`) |
| `dto` | Objetos de transferência e validação de entrada/saída (`records`) |
| `exception` | Exceções específicas de domínio |
| `model` | Entidades JPA e regras de negócio bancárias |
| `repository` | Interfaces de acesso e persistência de dados |
| `service` | Regras de aplicação e controle transacional |
| `util` | Configurações e formatação monetária (`MonetarioConfig`) |

## Funcionalidades e domínio bancário

- **Clientes:** cadastro e consulta de clientes (`PESSOA_FISICA` e `PESSOA_JURIDICA`) com unicidade de documento.
- **Contas Correntes:** abertura com limite especial e cobrança de taxa de manutenção.
- **Contas Poupança:** abertura com taxa de rendimento e aplicação de juros sobre saldo positivo.
- **Operações financeiras:** depósitos, saques, transferências entre contas e consulta de extrato.
- **Precisão monetária:** uso de `BigDecimal` com arredondamento bancário (`RoundingMode.HALF_EVEN`).

## Endpoints da API

### Clientes (`/api/clientes`)
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/clientes` | Cadastra um novo cliente |
| `GET` | `/api/clientes/{id}` | Busca um cliente pelo ID |

### Contas (`/api/contas`)
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/api/contas/corrente` | Abre uma conta corrente |
| `POST` | `/api/contas/poupanca` | Abre uma conta poupança |
| `GET` | `/api/contas` | Lista todas as contas |
| `GET` | `/api/contas/{numero}` | Busca uma conta pelo número |
| `GET` | `/api/contas/{numero}/extrato` | Lista as movimentações da conta |
| `POST` | `/api/contas/{numero}/depositos` | Realiza um depósito |
| `POST` | `/api/contas/{numero}/saques` | Realiza um saque |
| `POST` | `/api/contas/{numero}/transferencias` | Transfere valores para outra conta |
| `POST` | `/api/contas/{numero}/rendimentos` | Aplica rendimento (conta poupança) |
| `POST` | `/api/contas/{numero}/taxa-manutencao` | Cobra taxa de manutenção (conta corrente) |

## Como executar o projeto

### Pré-requisitos

- Java Development Kit (JDK) 17 ou superior.
- Apache Maven.
- Git.

### 1. Clonar o repositório

```bash
git clone https://github.com/vitoriagblima/bank-simulator
cd bank-simulator
```

### 2. Configurar o ambiente

Copie o arquivo de exemplo de configuração e ajuste os parâmetros do banco de dados conforme o seu ambiente:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

### 3. Compilar e executar os testes

```bash
mvn clean test
```

### 4. Inicializar a aplicação

```bash
mvn spring-boot:run
```

Ao iniciar fora do perfil `test`, a classe `DataInitializer` cria automaticamente dados de demonstração (conta corrente `1001` para João Silva e conta poupança `2001` para Maria Souza).

Com a aplicação em execução e o Swagger habilitado, a documentação interativa estará disponível em:

```text
http://localhost:8080/swagger-ui.html
```

## Testes

O projeto possui testes automatizados divididos em:

- **Testes de domínio (`ContaTest`):** validam regras de negócio, arredondamento e exceções sem dependência do Spring ou banco de dados.
- **Testes de integração (`ContaControllerTest`):** validam os endpoints REST, status HTTP e persistência em memória com H2 no perfil `test`.

Para executar toda a suíte:

```bash
mvn test
```

## Contribuição

Contribuições são bem-vindas! Confira o [Guia de Contribuição](./CONTRIBUTING.md) e o [Código de Conduta](./CODE_OF_CONDUCT.md) antes de enviar um Pull Request.

## Licença

Este projeto está distribuído sob os termos definidos no arquivo [LICENSE](./LICENSE).

## Aviso

Este projeto possui finalidade educacional e de estudo, voltado ao desenvolvimento de conhecimentos em Java, Spring Boot, arquitetura backend e engenharia de software.

Não se trata de um sistema bancário real e não deve ser utilizado para processar operações financeiras reais.
