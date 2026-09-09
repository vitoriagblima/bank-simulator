package application;

import model.entities.Banco;
import model.entities.Cliente;
import model.entities.ContaCorrente;
import model.entities.ContaPoupanca;
import model.entities.TipoCliente;
import model.exceptions.DomainException;

public class Testes {

    public static void main(String[] args) {
        Cliente carlos = new Cliente("Carlos", "123", TipoCliente.PESSOA_FISICA);
        Cliente ana = new Cliente("Ana", "456", TipoCliente.PESSOA_FISICA);

        System.out.println("=== MENSAGENS DE EXCEÇÕES LANÇADAS ===\n");

        // Depósito
        ContaCorrente cc = new ContaCorrente(1, 1, carlos, 100.0, 10.0);
        testarExcecao(() -> cc.depositar(-50.0), "Depósito negativo");
        testarExcecao(() -> cc.depositar(null), "Depósito nulo");
        testarExcecao(() -> cc.depositar(0.0), "Depósito zero");

        // Saque
        testarExcecao(() -> cc.sacar(200.0), "Saque além do limite");

        // Poupança
        ContaPoupanca cp = new ContaPoupanca(2, 1, ana, 0.05);
        testarExcecao(() -> cp.sacar(50.0), "Poupança sem saldo");
        testarExcecao(cp::renderJuros, "Rendimento sem saldo");

        // Transferência
        cc.depositar(100.0);
        testarExcecao(() -> cc.transferir(500.0, cp), "Transferência sem saldo");
        testarExcecao(() -> cc.transferir(10.0, null), "Transferência para conta nula");

        // Setters e Construtores - Conta Corrente
        testarExcecao(() -> cc.setLimiteEspecial(-10.0), "Set limite especial negativo");
        testarExcecao(() -> cc.setLimiteEspecial(null), "Set limite especial nulo");
        testarExcecao(() -> cc.setTaxaManutencao(-5.0), "Set taxa manutenção negativa");
        testarExcecao(() -> cc.setTaxaManutencao(null), "Set taxa manutenção nula");
        testarExcecao(() -> new ContaCorrente(4, 1, carlos, -100.0, 10.0), "Construtor ContaCorrente com limite negativo");

        // Setters e Construtores - Conta Poupança
        testarExcecao(() -> cp.setTaxaRendimento(0.0), "Set taxa rendimento zero");
        testarExcecao(() -> cp.setTaxaRendimento(-0.05), "Set taxa rendimento negativa");
        testarExcecao(() -> cp.setTaxaRendimento(null), "Set taxa rendimento nula");
        testarExcecao(() -> new ContaPoupanca(5, 1, ana, -0.01), "Construtor ContaPoupanca com taxa negativa");

        // Taxa
        ContaCorrente cc2 = new ContaCorrente(3, 1, carlos, 0.0, 0.0);
        testarExcecao(cc2::cobrarTaxaManutencao, "Taxa de manutenção zerada");

        // Banco
        Banco banco = new Banco("Banco Java");
        banco.adicionarConta(cc);

        testarExcecao(() -> banco.adicionarConta(null), "Adicionar conta nula");
        testarExcecao(() -> banco.adicionarConta(new ContaCorrente(1, 1, carlos, 50.0, 5.0)), "Adicionar conta com número duplicado");
        testarExcecao(() -> banco.buscarConta(null), "Buscar conta com número nulo");
        testarExcecao(() -> banco.buscarConta(-10), "Buscar conta com número negativo");
        testarExcecao(() -> banco.buscarConta(999), "Buscar conta inexistente");
    }

    private static void testarExcecao(Runnable acao, String cenario) {
        try {
            acao.run();
            System.out.println("[FALHA]: " + cenario + " não lançou exceção.");
        } catch (DomainException e) {
            System.out.println(cenario + " -> " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}