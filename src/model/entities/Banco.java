package model.entities;

import java.util.ArrayList;
import java.util.List;

import model.exceptions.DomainException;
import model.exceptions.ValorInvalidoException;

public class Banco {
    private String nome;
    private List<Conta> contas = new ArrayList<>();

    public Banco() {
    }

    public Banco(String nome) {
        this.nome = nome;
    }

    public void adicionarConta(Conta conta) {
        if (conta == null) {
            throw new ValorInvalidoException("A conta a ser adicionada não pode ser nula.");
        }
        for (Conta c : contas) {
            if (c.getNumero().equals(conta.getNumero())) {
                throw new DomainException("Já existe uma conta cadastrada com o número " + conta.getNumero() + ".");
            }
        }
        contas.add(conta);
    }

    public Conta buscarConta(Integer numero) {
        if (numero == null || numero <= 0) {
            throw new ValorInvalidoException("O número da conta deve ser maior que zero.");
        }
        for (Conta c : contas) {
            if (numero.equals(c.getNumero())) {
                return c;
            }
        }
        throw new DomainException("Conta com o número " + numero + " não foi encontrada.");
    }

    public void listarContas() {
        if (contas.isEmpty()) {
            System.out.println("Nenhuma conta cadastrada no banco.");
            return;
        }
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