package entities;

import java.util.List;
import java.util.ArrayList;

public class Banco {
    private String nome;

    List<Conta> contas = new ArrayList<>();

    public Banco() {
    }

    public Banco(String nome) {
        this.nome = nome;
    }

    public void adicionarConta(Conta conta) {
        contas.add(conta);
    }

    public Conta buscarConta(Integer numero) {
        if (numero != null && numero > 0) {
            for (Conta c : contas) {
                if (numero.equals(c.getNumero())) {
                    return c;
                }
            }
        }
        return null;
    }

    public void listarContas() {
        for (Conta c : contas) {
            System.out.println("Tipo: " + c.getClass().getSimpleName() +
                    " | Número da Conta: " + c.getNumero() +
                    " | Agência: " + c.getAgencia() +
                    " | Saldo: R$ " + String.format("%.2f", c.getSaldo()) +
                    " | Cliente: " + c.getTitular().getNome());
        }
    }

    public String getNome() {
        return this.nome;
    }
}
