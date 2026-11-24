# Guias de Contribuição — Bank Simulator

Obrigado pelo interesse em contribuir com este projeto! Este repositório é voltado ao estudo prático de **Java**, **Programação Orientada a Objetos (POO)** e **desenvolvimento backend com Spring Boot**, portanto toda contribuição, sugestão ou correção é muito bem-vinda.

---

## Como você pode contribuir?

1. **Reportando Bugs / Problemas:**
   - Abra uma *Issue* detalhando o erro encontrado.
   - Inclua os passos para reproduzir o problema, o payload enviado (quando aplicável) e os logs/resposta HTTP recebida.

2. **Sugerindo Melhorias:**
   - Ideias de refatoração, aprimoramento de regras de negócio (limites, taxas, validações), novos endpoints ou ampliação da cobertura de testes são muito bem-vindas.

3. **Enviando código (Pull Requests):**
   - Faça um **Fork** deste repositório.
   - Crie uma branch descritiva a partir da `main` (ex.: `feat/validacao-cpf` ou `fix/handler-domain-exception`).
   - Implemente as alterações e adicione testes automatizados cobrindo o novo comportamento.
   - Garanta que toda a suíte de testes passe com `mvn test`.
   - Abra um **Pull Request** apontando para a branch `main` explicando o que foi alterado e por quê.

---

## Padrões do Projeto

- **Linguagem e Stack:** Java 17 ou superior, Spring Boot, Maven, Spring Data JPA e JUnit 5.
- **Arquitetura em camadas:** Respeite a responsabilidade de cada pacote em `com.bancojava.simulador`:
  - `config` — configurações de inicialização e carga de dados (`DataInitializer`).
  - `controller` — endpoints REST e tratamento global de exceções (`GlobalExceptionHandler` com `ProblemDetail`).
  - `dto` — `records` de entrada (com Bean Validation) e saída de dados.
  - `exception` — exceções de domínio derivadas de `DomainException`.
  - `model` — entidades JPA e regras de negócio encapsuladas no domínio.
  - `repository` — interfaces Spring Data JPA.
  - `service` — orquestração de casos de uso e fronteiras transacionais (`@Transactional`).
  - `util` — utilitários e configurações monetárias (`MonetarioConfig`).
- **Regras de Domínio e Precisão Monetária:**
  - Utilize sempre `BigDecimal` com as configurações de `MonetarioConfig` (`HALF_EVEN`, escala 2 para valores monetários e escala 4 para taxas).
  - Mantenha regras de saldo, limites e lançamentos encapsuladas nas entidades (`Conta`, `ContaCorrente`, `ContaPoupanca`), evitando lógica de negócio nos controllers.
- **Testes:**
  - Regras puras de domínio devem ser testadas sem contexto Spring (como em `ContaTest`).
  - Fluxos HTTP e persistência devem ser testados via `MockMvc` no perfil `test` (como em `ContaControllerTest`).
- **Conduta:** Leia e siga o [Código de Conduta](./CODE_OF_CONDUCT.md).
