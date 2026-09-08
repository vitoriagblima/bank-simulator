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