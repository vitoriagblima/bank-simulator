# Bank Simulator

![Java](https://img.shields.io/badge/Java-Backend-000000?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-Framework-000000?style=for-the-badge&logo=springboot&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-Build%20Tool-000000?style=for-the-badge&logo=apachemaven&logoColor=white)
![JPA](https://img.shields.io/badge/Spring%20Data%20JPA-Persistence-000000?style=for-the-badge&logoColor=white)
![Status](https://img.shields.io/badge/Status-Em%20desenvolvimento-000000?style=for-the-badge)

## Sobre o projeto

O **Bank Simulator** é uma aplicação de simulação bancária desenvolvida em Java, com foco na aplicação de conceitos de Programação Orientada a Objetos, arquitetura de software e desenvolvimento backend.

O projeto iniciou como uma aplicação Java puro e evoluiu para uma arquitetura baseada em **Spring Boot e Maven**, incorporando conceitos de desenvolvimento de APIs REST, organização em camadas e persistência de dados.

Essa evolução representa uma nova etapa do projeto, buscando maior organização, escalabilidade, manutenção e aplicação de boas práticas de desenvolvimento.

## Evolução do projeto

O desenvolvimento foi dividido em duas etapas principais:

**Versão 1 — Java puro**
- Estrutura baseada em Programação Orientada a Objetos.
- Modelagem de entidades bancárias.
- Implementação de regras de negócio.
- Organização das responsabilidades da aplicação.

**Versão 2 — Spring Boot e Maven**
- Migração para uma arquitetura backend.
- Organização em camadas.
- Desenvolvimento com Spring Boot.
- Gerenciamento de dependências com Maven.
- Integração com Spring Data JPA e Hibernate.
- Utilização de banco de dados H2.
- Estruturação de endpoints REST.
- Organização de DTOs e tratamento de exceções.

## Tecnologias utilizadas

| Tecnologia | Finalidade |
|---|---|
| Java | Linguagem de programação |
| Spring Boot | Framework para desenvolvimento backend |
| Apache Maven | Gerenciamento de dependências e build |
| Spring Data JPA | Persistência e acesso a dados |
| Hibernate | Mapeamento objeto-relacional (ORM) |
| H2 Database | Banco de dados para desenvolvimento |
| REST API | Comunicação e exposição de recursos |
| Swagger / Springdoc | Documentação da API |
| JUnit | Testes automatizados |

## Arquitetura do projeto

A aplicação utiliza uma arquitetura organizada em camadas, buscando separar responsabilidades e facilitar a manutenção e evolução do código.

### Organização dos pacotes

| Pacote | Responsabilidade |
|---|---|
| `config` | Configurações da aplicação |
| `controller` | Recebimento e gerenciamento das requisições HTTP |
| `dto` | Transferência de dados entre as camadas |
| `exception` | Exceções e tratamento de erros |
| `model` | Entidades e representação do domínio bancário |
| `repository` | Acesso e persistência de dados |
| `service` | Implementação das regras de negócio |
| `util` | Classes auxiliares e utilitários |

## Conceitos aplicados

Durante o desenvolvimento, são trabalhados conceitos importantes da engenharia de software:

- Programação Orientada a Objetos (POO).
- Encapsulamento e abstração.
- Separação de responsabilidades.
- Arquitetura em camadas.
- Desenvolvimento de APIs REST.
- Transferência de dados com DTOs.
- Persistência e mapeamento objeto-relacional.
- Injeção de dependências.
- Tratamento centralizado de exceções.
- Validação de regras de negócio.
- Testes automatizados.
- Controle de versão com Git.

## Funcionalidades e domínio bancário

O projeto tem como objetivo simular operações e estruturas fundamentais de um ambiente bancário.

Entre os elementos trabalhados estão:

- Gerenciamento de clientes.
- Representação de contas bancárias.
- Contas correntes.
- Contas poupança.
- Operações e transações financeiras.
- Regras de movimentação bancária.
- Validações de operações.
- Tratamento de situações excepcionais.

## Banco de dados

A aplicação utiliza o H2 Database integrado ao Spring Data JPA e Hibernate para trabalhar com persistência de dados durante o desenvolvimento.

A camada de persistência é responsável por intermediar o acesso aos dados e manter a separação entre as regras de negócio e a infraestrutura do banco.

As configurações internas de ambiente são mantidas fora da documentação pública.

## Como executar o projeto

### Pré-requisitos

- Java Development Kit (JDK) compatível com a versão definida no projeto.
- Apache Maven.
- Git.
- IDE de sua preferência (IntelliJ IDEA, Eclipse ou Visual Studio Code).

### 1. Clonar o repositório

```bash
git clone https://github.com/vitoriagblima/bank-simulator
```

### 2. Acessar a pasta do projeto

```bash
cd bank-simulator
```

### 3. Compilar a aplicação

```bash
mvn clean install
```

### 4. Executar os testes

```bash
mvn test
```

### 5. Inicializar a aplicação

```bash
mvn spring-boot:run
```

A aplicação será iniciada conforme as configurações definidas no ambiente de execução.

## Testes

O projeto possui uma estrutura dedicada aos testes automatizados, permitindo verificar o comportamento dos componentes e a aplicação das regras de negócio.

Para executar a suíte de testes:

```bash
mvn test
```

## Boas práticas adotadas

- Separação de responsabilidades.
- Organização modular do código.
- Utilização de padrões de arquitetura.
- Centralização do tratamento de exceções.
- Reutilização de componentes.
- Organização das dependências.
- Controle de versão com Git.
- Separação entre código-fonte e configurações de ambiente.

## Controle de versão

O histórico de desenvolvimento é organizado utilizando Git e GitHub.

A evolução do projeto mantém a versão inicial em Java puro preservada por meio de versionamento, enquanto a nova arquitetura representa uma etapa independente de evolução.

## Próximas evoluções

- Ampliação da cobertura de testes automatizados.
- Aprimoramento das validações de negócio.
- Evolução dos recursos da API REST.
- Melhorias na documentação técnica.
- Implementação de novos recursos bancários.
- Aprimoramento da persistência de dados.
- Evolução da segurança da aplicação.

## Licença

Este projeto está distribuído sob os termos definidos no arquivo [LICENSE](./LICENSE).

## Aviso

Este projeto possui finalidade educacional e de estudo, voltado ao desenvolvimento de conhecimentos em Java, Spring Boot, arquitetura backend e engenharia de software.

Não se trata de um sistema bancário real e não deve ser utilizado para processar operações financeiras reais.

---

**Bank Simulator**  
*Evoluindo conhecimentos em Java e desenvolvimento backend.*
