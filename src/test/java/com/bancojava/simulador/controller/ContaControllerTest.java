
package com.bancojava.simulador.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.jayway.jsonpath.JsonPath;

/**
 * Testes de integração da API REST com H2 em memória.
 * Cada método roda em uma transação que é desfeita ao final (@Transactional).
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ContaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void fluxoCompletoDeCadastroDepositoETransferencia() throws Exception {

        long joao = criarCliente("João Silva", "111.111.111-11");
        long maria = criarCliente("Maria Souza", "222.222.222-22");

        mvc.perform(post("/api/contas/corrente")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numero":1001,"agencia":1,"clienteId":%d,
                                 "limiteEspecial":500.00,"taxaManutencao":20.00}
                                """.formatted(joao)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.tipo").value("Conta Corrente"));

        mvc.perform(post("/api/contas/poupanca")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numero":2001,"agencia":1,"clienteId":%d,"taxaRendimento":0.0050}
                                """.formatted(maria)))
                .andExpect(status().isCreated());

        mvc.perform(post("/api/contas/1001/depositos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valor\":1000.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(1000.00));

        mvc.perform(post("/api/contas/1001/transferencias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"numeroDestino\":2001,\"valor\":100.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(900.00));

        mvc.perform(get("/api/contas/2001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.saldo").value(100.00));

        mvc.perform(get("/api/contas/1001/extrato"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void saqueAcimaDoDisponivelRetorna422() throws Exception {

        long joao = criarCliente("João Silva", "333.333.333-33");
        abrirPoupanca(3001, joao);

        mvc.perform(post("/api/contas/3001/saques")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valor\":50.00}"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422));
    }

    @Test
    void aplicarRendimentoEmPoupancaSemSaldoPositivoRetorna422()
            throws Exception {

        long clienteId = criarCliente(
                "Cliente RND03",
                "666.666.666-66"
        );

        abrirPoupanca(6001, clienteId);

        mvc.perform(post("/api/contas/6001/rendimentos"))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.detail").value(
                        "Não é possível aplicar rendimentos em uma conta sem saldo positivo."
                ));
    }

    @Test
    void contaInexistenteRetorna404() throws Exception {

        mvc.perform(get("/api/contas/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void numeroDeContaDuplicadoRetorna409() throws Exception {

        long joao = criarCliente("João Silva", "444.444.444-44");
        abrirPoupanca(4001, joao);

        mvc.perform(post("/api/contas/poupanca")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numero":4001,"agencia":1,"clienteId":%d,"taxaRendimento":0.0050}
                                """.formatted(joao)))
                .andExpect(status().isConflict());
    }

    @Test
    void valorInvalidoRetorna400() throws Exception {

        long joao = criarCliente("João Silva", "555.555.555-55");
        abrirPoupanca(5001, joao);

        mvc.perform(post("/api/contas/5001/depositos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"valor\":-10}"))
                .andExpect(status().isBadRequest());
    }

    // ----------------------------- Auxiliares -----------------------------

    private long criarCliente(String nome, String documento) throws Exception {

        MvcResult resultado = mvc.perform(post("/api/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nome":"%s","documento":"%s","tipo":"PESSOA_FISICA"}
                                """.formatted(nome, documento)))
                .andExpect(status().isCreated())
                .andReturn();

        return ((Number) JsonPath.read(
                resultado.getResponse().getContentAsString(),
                "$.id"
        )).longValue();
    }

    private void abrirPoupanca(int numero, long clienteId) throws Exception {

        mvc.perform(post("/api/contas/poupanca")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"numero":%d,"agencia":1,"clienteId":%d,"taxaRendimento":0.0050}
                                """.formatted(numero, clienteId)))
                .andExpect(status().isCreated());
    }
}
