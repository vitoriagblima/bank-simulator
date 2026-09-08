package model.entities;

import java.util.ArrayList;
import java.util.List;

import model.exceptions.SaldoInsuficienteException;
import model.exceptions.ValorInvalidoException;

public abstract class Conta {
    private Long id;
    private Integer numero;
    private Integer agencia;
    protected Double saldo;
    private Cliente titular;

    private List<Transacao> transacoes = new ArrayList<>();

    public Conta() {
        this.saldo = 0.0;
    }

    public Conta(Integer numero, Integer agencia, Cliente cliente) {
        this.numero = numero;
        this.agencia = agencia;
        this.titular = cliente;
        this.saldo = 0.0;
    }

    public void depositar(Double valor) {
        if (valor == null) {
            throw new ValorInvalidoException("O valor do depósito não pode ser nulo.");
        }
        if (valor <= 0) {
            throw new ValorInvalidoException("O valor do depósito deve ser maior que zero.");
        }
        this.saldo += valor;
        adicionarTransacao(valor, TipoTransacao.DEPOSITO, "Depósito");
    }

    public void sacar(Double valor) {
        sacar(valor, TipoTransacao.SAQUE, "Saque");
    }

    public void sacar(Double valor, TipoTransacao tipo, String descricao) {
        if (valor == null) {
            throw new ValorInvalidoException("O valor do saque não pode ser nulo.");
        }
        if (valor <= 0) {
            throw new ValorInvalidoException("O valor do saque deve ser maior que zero.");
        }
        if (valor > saldo) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar o saque.");
        }
        this.saldo -= valor;
        adicionarTransacao(valor, tipo, descricao);
    }

    public void transferir(Double valor, Conta destino) {
        if (destino == null) {
            throw new ValorInvalidoException("A conta a ser transferida não pode ser nula.");
        }
        String descricao = "Transferência para " + destino.getTitular().getNome();
        this.sacar(valor, TipoTransacao.TRANSFERENCIA, descricao);
        destino.depositar(valor);
    }

    protected void adicionarTransacao(Double valor, TipoTransacao tipo, String desc) {
        Transacao novaTransacao = new Transacao(valor, tipo, desc);
        transacoes.add(novaTransacao);
    }

    public void imprimirExtrato() {
        for (Transacao t : transacoes) {
            System.out.println(t);
        }
    }

    public Long getId() {
        return this.id;
    }

    public Integer getNumero() {
        return this.numero;
    }

    public Integer getAgencia() {
        return this.agencia;
    }

    public Double getSaldo() {
        return this.saldo;
    }

    public Cliente getTitular() {
        return this.titular;
    }

    @Override
    public String toString() {
        return "Conta [numero=" + numero + ", agencia="
                + agencia + ", saldo=" + saldo + ", titular=" + titular + "]";
    }
}