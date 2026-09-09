package model.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import model.exceptions.SaldoInsuficienteException;
import model.exceptions.ValorInvalidoException;

public abstract class Conta {
    private Long id;
    private Integer numero;
    private Integer agencia;
    protected BigDecimal saldo;
    private Cliente titular;

    private List<Transacao> transacoes = new ArrayList<>();

    public Conta() {
        this.saldo = BigDecimal.ZERO;
    }

    public Conta(Integer numero, Integer agencia, Cliente cliente) {
        this.numero = numero;
        this.agencia = agencia;
        this.titular = cliente;
        this.saldo = BigDecimal.ZERO;
    }

    public void depositar(BigDecimal valor) {
        if (valor == null) {
            throw new ValorInvalidoException("O valor do depósito não pode ser nulo.");
        }
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("O valor do depósito deve ser maior que zero.");
        }
        this.saldo = this.saldo.add(valor);
        adicionarTransacao(valor, TipoTransacao.DEPOSITO, "Depósito");
    }

    public void sacar(BigDecimal valor) {
        sacar(valor, TipoTransacao.SAQUE, "Saque");
    }

    public void sacar(BigDecimal valor, TipoTransacao tipo, String descricao) {
        if (valor == null) {
            throw new ValorInvalidoException("O valor do saque não pode ser nulo.");
        }
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("O valor do saque deve ser maior que zero.");
        }
        if (valor.compareTo(saldo) > 0) {
            throw new SaldoInsuficienteException("Saldo insuficiente para realizar o saque.");
        }
        this.saldo = this.saldo.subtract(valor);
        adicionarTransacao(valor, tipo, descricao);
    }

    public void transferir(BigDecimal valor, Conta destino) {
        if (destino == null) {
            throw new ValorInvalidoException("A conta a ser transferida não pode ser nula.");
        }
        String descricao = "Transferência para " + destino.getTitular().getNome();
        this.sacar(valor, TipoTransacao.TRANSFERENCIA, descricao);
        destino.depositar(valor);
    }

    protected void adicionarTransacao(BigDecimal valor, TipoTransacao tipo, String desc) {
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

    public BigDecimal getSaldo() {
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