package application;

import java.math.BigDecimal;

import model.entities.Banco;
import model.entities.Cliente;
import model.entities.Conta;
import model.entities.ContaCorrente;
import model.entities.ContaPoupanca;
import model.entities.TipoCliente;
import model.exceptions.DomainException;
import model.exceptions.SaldoInsuficienteException;
import model.exceptions.ValorInvalidoException;

public class Testes {

    private static int aprovados = 0;
    private static int falhas = 0;

    public static void main(String[] args) {
        System.out.println("=== TESTES DE REGRAS DE NEGÓCIO ===\n");

        Cliente carlos = new Cliente("Carlos", "123", TipoCliente.PESSOA_FISICA);
        Cliente ana = new Cliente("Ana", "456", TipoCliente.PESSOA_FISICA);

        // ---------- Cliente ----------
        esperarExcecao(ValorInvalidoException.class,
                () -> new Cliente("   ", "123", TipoCliente.PESSOA_FISICA), "Cliente com nome em branco");
        esperarExcecao(ValorInvalidoException.class,
                () -> new Cliente("X", null, TipoCliente.PESSOA_FISICA), "Cliente sem documento");
        esperarExcecao(ValorInvalidoException.class,
                () -> new Cliente("X", "1", null), "Cliente sem tipo");

        // ---------- Depósito ----------
        ContaCorrente cc = new ContaCorrente(1, 1, carlos, new BigDecimal("100.00"), new BigDecimal("10.00"));
        esperarExcecao(ValorInvalidoException.class,
                () -> cc.depositar(new BigDecimal("-50")), "Depósito negativo");
        esperarExcecao(ValorInvalidoException.class,
                () -> cc.depositar(null), "Depósito nulo");
        esperarExcecao(ValorInvalidoException.class,
                () -> cc.depositar(BigDecimal.ZERO), "Depósito zero");

        // ---------- Saque e limite especial ----------
        esperarExcecao(SaldoInsuficienteException.class,
                () -> cc.sacar(new BigDecimal("200")), "Saque além do limite");
        esperarExcecao(ValorInvalidoException.class,
                () -> cc.sacar(null), "Saque nulo");
        cc.sacar(new BigDecimal("100"));
        esperarValor("-100.00", cc.getSaldo().toString(), "Saque até o limite especial");
        esperarExcecao(SaldoInsuficienteException.class,
                () -> cc.sacar(new BigDecimal("0.01")), "Saque de centavo após esgotar o limite");

        // ---------- Poupança ----------
        ContaPoupanca cp = new ContaPoupanca(2, 1, ana, new BigDecimal("0.05"));
        esperarExcecao(SaldoInsuficienteException.class,
                () -> cp.sacar(new BigDecimal("50")), "Poupança sem saldo");
        esperarExcecao(DomainException.class,
                cp::renderJuros, "Rendimento sem saldo positivo");

        // ---------- Transferência ----------
        ContaCorrente origem = new ContaCorrente(10, 1, carlos, BigDecimal.ZERO, BigDecimal.ZERO);
        ContaPoupanca destino = new ContaPoupanca(11, 1, ana, new BigDecimal("0.01"));
        origem.depositar(new BigDecimal("100"));
        esperarExcecao(SaldoInsuficienteException.class,
                () -> origem.transferir(new BigDecimal("500"), destino), "Transferência acima do saldo");
        esperarExcecao(ValorInvalidoException.class,
                () -> origem.transferir(new BigDecimal("10"), null), "Transferência para conta nula");
        esperarExcecao(ValorInvalidoException.class,
                () -> origem.transferir(new BigDecimal("10"), origem), "Transferência para a própria conta");
        origem.transferir(new BigDecimal("40"), destino);
        esperarValor("60.00", origem.getSaldo().toString(), "Saldo do remetente após transferência");
        esperarValor("40.00", destino.getSaldo().toString(), "Saldo do destinatário após transferência");
        esperarValor("TRANSFERENCIA_RECEBIDA", destino.getTransacoes().get(0).getTipo().name(),
                "Tipo de lançamento do destinatário");

        // ---------- Setters e construtores ----------
        esperarExcecao(ValorInvalidoException.class,
                () -> cc.setLimiteEspecial(new BigDecimal("-10")), "Set limite especial negativo");
        esperarExcecao(ValorInvalidoException.class,
                () -> cc.setLimiteEspecial(null), "Set limite especial nulo");
        esperarExcecao(ValorInvalidoException.class,
                () -> cc.setTaxaManutencao(new BigDecimal("-5")), "Set taxa de manutenção negativa");
        esperarExcecao(ValorInvalidoException.class,
                () -> cc.setTaxaManutencao(null), "Set taxa de manutenção nula");
        esperarExcecao(ValorInvalidoException.class,
                () -> new ContaCorrente(4, 1, carlos, new BigDecimal("-100"), BigDecimal.TEN),
                "Construtor ContaCorrente com limite negativo");
        esperarExcecao(ValorInvalidoException.class,
                () -> cp.setTaxaRendimento(BigDecimal.ZERO), "Set taxa de rendimento zero");
        esperarExcecao(ValorInvalidoException.class,
                () -> cp.setTaxaRendimento(new BigDecimal("-0.05")), "Set taxa de rendimento negativa");
        esperarExcecao(ValorInvalidoException.class,
                () -> cp.setTaxaRendimento(null), "Set taxa de rendimento nula");
        esperarExcecao(ValorInvalidoException.class,
                () -> new ContaPoupanca(5, 1, ana, new BigDecimal("-0.01")),
                "Construtor ContaPoupanca com taxa negativa");

        // ---------- Taxa de manutenção ----------
        ContaCorrente semTaxa = new ContaCorrente(3, 1, carlos, BigDecimal.ZERO, BigDecimal.ZERO);
        esperarExcecao(ValorInvalidoException.class,
                semTaxa::cobrarTaxaManutencao, "Cobrança com taxa zerada");
        ContaCorrente semSaldo = new ContaCorrente(6, 1, carlos, BigDecimal.ZERO, new BigDecimal("10"));
        esperarExcecao(SaldoInsuficienteException.class,
                semSaldo::cobrarTaxaManutencao, "Cobrança de taxa sem saldo nem limite");

        // ---------- Banco ----------
        Banco banco = new Banco("Banco Java");
        banco.adicionarConta(new ContaCorrente(1, 1, carlos, new BigDecimal("50"), new BigDecimal("5")));

        esperarExcecao(ValorInvalidoException.class,
                () -> new Banco("   "), "Banco sem nome");
        esperarExcecao(ValorInvalidoException.class,
                () -> banco.adicionarConta(null), "Adicionar conta nula");
        esperarExcecao(ValorInvalidoException.class,
                () -> banco.adicionarConta(new ContaPoupanca()), "Adicionar conta sem número");
        esperarExcecao(DomainException.class,
                () -> banco.adicionarConta(new ContaCorrente(1, 1, carlos, BigDecimal.ONE, BigDecimal.ONE)),
                "Adicionar conta com número duplicado");
        esperarExcecao(ValorInvalidoException.class,
                () -> banco.buscarConta(null), "Buscar conta com número nulo");
        esperarExcecao(ValorInvalidoException.class,
                () -> banco.buscarConta(-10), "Buscar conta com número negativo");
        esperarExcecao(DomainException.class,
                () -> banco.buscarConta(999), "Buscar conta inexistente");
        esperarValor("1", banco.buscarConta(1).getNumero().toString(), "Buscar conta existente");

        // ---------- Arredondamento ----------
        System.out.println("\n=== ARREDONDAMENTO BANCÁRIO (HALF_EVEN) ===");
        esperarValor("0.0022", ContaPoupanca.normalizarTaxa(new BigDecimal("0.00225")).toString(),
                "Taxa 0,00225 -> 0,0022");
        esperarValor("2.34", Conta.normalizar(new BigDecimal("2.345")).toString(), "Valor 2,345 -> 2,34");
        esperarValor("2.36", Conta.normalizar(new BigDecimal("2.355")).toString(), "Valor 2,355 -> 2,36");

        ContaPoupanca poupanca = new ContaPoupanca(7, 1, ana, new BigDecimal("0.0050"));
        poupanca.depositar(new BigDecimal("1.25"));
        poupanca.renderJuros();
        esperarValor("1.26", poupanca.getSaldo().toString(),
                "Rendimento de 1,25 x 0,5% = 0,00625 -> 0,01 (arredondamento único)");

        // ---------- Resultado ----------
        System.out.println("\nResultado: " + aprovados + " aprovados, " + falhas + " falhas.");
        if (falhas > 0) {
            System.exit(1);
        }
    }

    private static void esperarExcecao(Class<? extends DomainException> esperada, Runnable acao, String cenario) {
        try {
            acao.run();
            reprovar(cenario, "nenhuma exceção foi lançada");
        } catch (DomainException e) {
            if (esperada.isInstance(e)) {
                aprovar(cenario + " -> " + e.getClass().getSimpleName() + ": " + e.getMessage());
            } else {
                reprovar(cenario, "esperava " + esperada.getSimpleName()
                        + ", recebeu " + e.getClass().getSimpleName());
            }
        }
    }

    private static void esperarValor(String esperado, String obtido, String cenario) {
        if (esperado.equals(obtido)) {
            aprovar(cenario + " -> " + obtido);
        } else {
            reprovar(cenario, "esperado " + esperado + ", obtido " + obtido);
        }
    }

    private static void aprovar(String mensagem) {
        aprovados++;
        System.out.println("[OK]    " + mensagem);
    }

    private static void reprovar(String cenario, String motivo) {
        falhas++;
        System.out.println("[FALHA] " + cenario + " -> " + motivo);
    }
}