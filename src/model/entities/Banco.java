package model.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import model.exceptions.DomainException;
import model.exceptions.ValorInvalidoException;

public class Banco {

    private final String nome;
    private final Map<Integer, Conta> contas = new LinkedHashMap<>();

    public Banco(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new ValorInvalidoException("O nome do banco não pode ser nulo ou vazio.");
        }
        this.nome = nome.trim();
    }

    public void adicionarConta(Conta conta) {
        if (conta == null) {
            throw new ValorInvalidoException("A conta a ser adicionada não pode ser nula.");
        }
        if (conta.getNumero() == null) {
            throw new ValorInvalidoException("A conta precisa ter um número válido antes de ser cadastrada.");
        }
        if (contas.containsKey(conta.getNumero())) {
            throw new DomainException("Já existe uma conta cadastrada com o número " + conta.getNumero() + ".");
        }
        contas.put(conta.getNumero(), conta);
    }

    public Conta buscarConta(Integer numero) {
        if (numero == null || numero <= 0) {
            throw new ValorInvalidoException("O número da conta deve ser maior que zero.");
        }
        Conta conta = contas.get(numero);
        if (conta == null) {
            throw new DomainException("Conta com o número " + numero + " não foi encontrada.");
        }
        return conta;
    }

    public List<Conta> getContas() {
        return Collections.unmodifiableList(new ArrayList<>(contas.values()));
    }

    public void listarContas() {
        if (contas.isEmpty()) {
            System.out.println("Nenhuma conta cadastrada no banco.");
            return;
        }
        for (Conta c : contas.values()) {
            System.out.println(c);
        }
    }

    public String getNome() {
        return this.nome;
    }
}