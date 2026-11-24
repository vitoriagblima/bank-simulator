package com.bancojava.simulador.config;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.bancojava.simulador.dto.ClienteDto;
import com.bancojava.simulador.dto.ContaDto;
import com.bancojava.simulador.model.TipoCliente;
import com.bancojava.simulador.service.ClienteService;
import com.bancojava.simulador.service.ContaService;

/**
 * Carrega o mesmo cenário de demonstração do antigo Main.java.
 * Desativado no perfil "test" para que os testes partam de um banco vazio.
 */
@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final ClienteService clienteService;
    private final ContaService contaService;

    public DataInitializer(ClienteService clienteService, ContaService contaService) {
        this.clienteService = clienteService;
        this.contaService = contaService;
    }

    @Override
    public void run(String... args) {
        ClienteDto.Response joao = clienteService.cadastrar(
                new ClienteDto.Request("João Silva", "123.456.789-00", TipoCliente.PESSOA_FISICA));
        ClienteDto.Response maria = clienteService.cadastrar(
                new ClienteDto.Request("Maria Souza", "987.654.321-00", TipoCliente.PESSOA_FISICA));

        contaService.abrirCorrente(new ContaDto.CorrenteRequest(
                1001, 1, joao.id(), new BigDecimal("500.00"), new BigDecimal("20.00")));
        contaService.abrirPoupanca(new ContaDto.PoupancaRequest(
                2001, 1, maria.id(), new BigDecimal("0.0050")));

        contaService.depositar(1001, new BigDecimal("1000.00"));
        contaService.depositar(2001, new BigDecimal("2000.00"));
        contaService.sacar(1001, new BigDecimal("1300.00"));   // usa o limite especial
        contaService.sacar(2001, new BigDecimal("200.00"));
        contaService.transferir(1001, new ContaDto.Transferencia(2001, new BigDecimal("100.00")));
        contaService.aplicarRendimento(2001);
        contaService.cobrarTaxaManutencao(1001);

        log.info("Dados de demonstração carregados: conta corrente 1001 (João) e poupança 2001 (Maria).");
    }
}