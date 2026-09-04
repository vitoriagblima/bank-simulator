package application;

import java.util.TimeZone;

import entities.Banco;
import entities.Cliente;
import entities.Conta;
import entities.ContaCorrente;
import entities.ContaPoupanca;
import entities.TipoCliente;

public class Main {

    public static void main(String[] args) {

        TimeZone.setDefault(TimeZone.getTimeZone("America/Sao_Paulo"));
        Banco banco = new Banco("Banco Java");

        Cliente joao = new Cliente("João Silva", "123.456.789-00", TipoCliente.PESSOA_FISICA);
        Cliente maria = new Cliente("Maria Souza", "987.654.321-00", TipoCliente.PESSOA_FISICA);

        ContaCorrente ccJoao = new ContaCorrente(1001, 1, joao, 500.0, 20.0);
        ContaPoupanca cpMaria = new ContaPoupanca(2001, 1, maria, 0.005);

        banco.adicionarConta(ccJoao);
        banco.adicionarConta(cpMaria);

        System.out.println("=== 1. CONTAS RECÉM-CRIADAS (SALDO INICIAL) ===");
        banco.listarContas();

        System.out.println("\n=== 2. REALIZANDO MOVIMENTAÇÕES ===");

        ccJoao.depositar(1000.0);
        cpMaria.depositar(2000.0);

        boolean saque1 = ccJoao.sacar(1300.0);
        System.out.println(String.format("Saque de R$ 1300,00 na Conta Corrente: %b", saque1));

        boolean saque2 = cpMaria.sacar(5000.0);
        System.out.println(String.format("Saque de R$ 5000,00 na Conta Poupança: %b", saque2));

        ccJoao.transferir(100.0, cpMaria);
        cpMaria.renderJuros();
        ccJoao.cobrarTaxaManutencao();

        System.out.println("\n=== 3. SALDOS FINAIS COM TIPO DE CONTA ===");
        System.out.println(ccJoao);
        System.out.println(cpMaria);

        System.out.println("\n=== 4. EXTRATO - CONTA CORRENTE (JOÃO) ===");
        ccJoao.imprimirExtrato();

        System.out.println("\n=== 5. EXTRATO - CONTA POUPANÇA (MARIA) ===");
        cpMaria.imprimirExtrato();

        System.out.println("\n=== 6. BUSCA DE CONTA PELO NÚMERO ===");
        Conta busca1 = banco.buscarConta(1001);
        System.out.println(busca1);

    }
}