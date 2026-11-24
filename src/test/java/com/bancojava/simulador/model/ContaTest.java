package com.bancojava.simulador.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.bancojava.simulador.exception.DomainException;
import com.bancojava.simulador.exception.SaldoInsuficienteException;
import com.bancojava.simulador.exception.ValorInvalidoException;

/** Regras de negócio do domínio, sem Spring e sem banco de dados. */
class ContaTest {

    private final Cliente carlos = new Cliente("Carlos", "123", TipoCliente.PESSOA_FISICA);
    private final Cliente ana = new Cliente("Ana", "456", TipoCliente.PESSOA_FISICA);

    @Test
    void clienteInvalidoNaoPodeSerCriado() {
        assertThrows(ValorInvalidoException.class, () -> new Cliente("   ", "123", TipoCliente.PESSOA_FISICA));
        assertThrows(ValorInvalidoException.class, () -> new Cliente("X", null, TipoCliente.PESSOA_FISICA));
        assertThrows(ValorInvalidoException.class, () -> new Cliente("X", "1", null));
    }

    @Test
    void depositoDeveSerPositivo() {
        ContaCorrente cc = new ContaCorrente(1, 1, carlos, new BigDecimal("100.00"), new BigDecimal("10.00"));
        assertThrows(ValorInvalidoException.class, () -> cc.depositar(new BigDecimal("-50")));
        assertThrows(ValorInvalidoException.class, () -> cc.depositar(null));
        assertThrows(ValorInvalidoException.class, () -> cc.depositar(BigDecimal.ZERO));
    }

    @Test
    void saqueUsaLimiteEspecialEMasNaoUltrapassaO() {
        ContaCorrente cc = new ContaCorrente(1, 1, carlos, new BigDecimal("100.00"), new BigDecimal("10.00"));

        assertThrows(SaldoInsuficienteException.class, () -> cc.sacar(new BigDecimal("200")));
        cc.sacar(new BigDecimal("100"));
        assertEquals(new BigDecimal("-100.00"), cc.getSaldo());
        assertThrows(SaldoInsuficienteException.class, () -> cc.sacar(new BigDecimal("0.01")));
    }

    @Test
    void poupancaNaoPermiteSaqueSemSaldo() {
        ContaPoupanca cp = new ContaPoupanca(2, 1, ana, new BigDecimal("0.05"));
        assertThrows(SaldoInsuficienteException.class, () -> cp.sacar(new BigDecimal("50")));
        assertThrows(DomainException.class, cp::renderJuros);
    }

    @Test
    void transferenciaMovimentaOsDoisLadosEAbreLancamentoDeRecebimento() {
        ContaCorrente origem = new ContaCorrente(10, 1, carlos, BigDecimal.ZERO, BigDecimal.ZERO);
        ContaPoupanca destino = new ContaPoupanca(11, 1, ana, new BigDecimal("0.01"));
        origem.depositar(new BigDecimal("100"));

        assertThrows(SaldoInsuficienteException.class,
                () -> origem.transferir(new BigDecimal("500"), destino));
        assertThrows(ValorInvalidoException.class,
                () -> origem.transferir(new BigDecimal("10"), null));
        assertThrows(ValorInvalidoException.class,
                () -> origem.transferir(new BigDecimal("10"), origem));

        origem.transferir(new BigDecimal("40"), destino);

        assertEquals(new BigDecimal("60.00"), origem.getSaldo());
        assertEquals(new BigDecimal("40.00"), destino.getSaldo());
        assertEquals(TipoTransacao.TRANSFERENCIA_RECEBIDA, destino.getTransacoes().get(0).getTipo());
    }

    @Test
    void settersValidamLimitesETaxas() {
        ContaCorrente cc = new ContaCorrente(1, 1, carlos, BigDecimal.ZERO, BigDecimal.ZERO);
        ContaPoupanca cp = new ContaPoupanca(2, 1, ana, new BigDecimal("0.05"));

        assertThrows(ValorInvalidoException.class, () -> cc.setLimiteEspecial(new BigDecimal("-10")));
        assertThrows(ValorInvalidoException.class, () -> cc.setLimiteEspecial(null));
        assertThrows(ValorInvalidoException.class, () -> cc.setTaxaManutencao(new BigDecimal("-5")));
        assertThrows(ValorInvalidoException.class, () -> cc.setTaxaManutencao(null));
        assertThrows(ValorInvalidoException.class, () -> cp.setTaxaRendimento(BigDecimal.ZERO));
        assertThrows(ValorInvalidoException.class, () -> cp.setTaxaRendimento(new BigDecimal("-0.05")));
        assertThrows(ValorInvalidoException.class, () -> cp.setTaxaRendimento(null));
    }

    @Test
    void cobrancaDeTaxaExigeTaxaESaldoDisponivel() {
        ContaCorrente semTaxa = new ContaCorrente(3, 1, carlos, BigDecimal.ZERO, BigDecimal.ZERO);
        assertThrows(ValorInvalidoException.class, semTaxa::cobrarTaxaManutencao);

        ContaCorrente semSaldo = new ContaCorrente(6, 1, carlos, BigDecimal.ZERO, new BigDecimal("10"));
        assertThrows(SaldoInsuficienteException.class, semSaldo::cobrarTaxaManutencao);
    }

    @Test
    void arredondamentoBancarioHalfEven() {
        assertEquals(new BigDecimal("0.0022"),
                ContaPoupanca.normalizarTaxa(new BigDecimal("0.00225")));
        assertEquals(new BigDecimal("2.34"), Conta.normalizar(new BigDecimal("2.345")));
        assertEquals(new BigDecimal("2.36"), Conta.normalizar(new BigDecimal("2.355")));
    }

    @Test
    void rendimentoArredondaUmaUnicaVez() {
        ContaPoupanca poupanca = new ContaPoupanca(7, 1, ana, new BigDecimal("0.0050"));
        poupanca.depositar(new BigDecimal("1.25"));
        poupanca.renderJuros();
        // 1,25 x 0,5% = 0,00625 -> 0,01
        assertEquals(new BigDecimal("1.26"), poupanca.getSaldo());
    }
}