package application;

import java.math.BigDecimal;
import java.util.TimeZone;

import model.entities.Banco;
import model.entities.Cliente;
import model.entities.ContaCorrente;
import model.entities.ContaPoupanca;
import model.entities.TipoCliente;
import model.exceptions.DomainException;

public class Main {

    public static void main(String[] args) {

        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));

        Banco banco = new Banco("Banco Java");

        Cliente joao = new Cliente("João Silva", "123.456.789-00", TipoCliente.PESSOA_FISICA);
        Cliente maria = new Cliente("Maria Souza", "987.654.321-00", TipoCliente.PESSOA_FISICA);

        ContaCorrente ccJoao = new ContaCorrente(1001, 1, joao, new BigDecimal("500.00"), new BigDecimal("20.00"));
        ContaPoupanca cpMaria = new ContaPoupanca(2001, 1, maria, new BigDecimal("0.0050"));

        banco.adicionarConta(ccJoao);
        banco.adicionarConta(cpMaria);

        secao("1. CONTAS RECÉM-CRIADAS");
        banco.listarContas();

        // Cada operação é executada isoladamente: um erro não interrompe a simulação.
        secao("2. MOVIMENTAÇÕES");
        executar("Depósito de R$ 1.000,00 na conta corrente",
                () -> ccJoao.depositar(new BigDecimal("1000.00")));
        executar("Depósito de R$ 2.000,00 na poupança",
                () -> cpMaria.depositar(new BigDecimal("2000.00")));
        executar("Saque de R$ 1.300,00 na conta corrente (usa limite especial)",
                () -> ccJoao.sacar(new BigDecimal("1300.00")));
        executar("Saque de R$ 200,00 na poupança",
                () -> cpMaria.sacar(new BigDecimal("200.00")));
        executar("Transferência de R$ 100,00 de João para Maria",
                () -> ccJoao.transferir(new BigDecimal("100.00"), cpMaria));
        executar("Aplicação de rendimento na poupança",
                cpMaria::renderJuros);
        executar("Cobrança de taxa de manutenção na conta corrente",
                ccJoao::cobrarTaxaManutencao);
        executar("Saque de R$ 5.000,00 na poupança (erro esperado)",
                () -> cpMaria.sacar(new BigDecimal("5000.00")));

        secao("3. SALDOS FINAIS");
        System.out.println(ccJoao);
        System.out.println(cpMaria);

        secao("4. EXTRATO - CONTA CORRENTE (JOÃO)");
        ccJoao.imprimirExtrato();

        secao("5. EXTRATO - CONTA POUPANÇA (MARIA)");
        cpMaria.imprimirExtrato();

        secao("6. BUSCA DE CONTA PELO NÚMERO");
        executar("Buscar conta 1001", () -> System.out.println(banco.buscarConta(1001)));
        executar("Buscar conta 9999 (erro esperado)", () -> banco.buscarConta(9999));
    }

    private static void secao(String titulo) {
        System.out.println("\n=== " + titulo + " ===");
    }

    private static void executar(String descricao, Runnable operacao) {
        try {
            operacao.run();
            System.out.println("[OK]   " + descricao);
        } catch (DomainException e) {
            System.out.println("[ERRO] " + descricao + " -> " + e.getMessage());
        }
    }
}