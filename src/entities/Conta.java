package entities;

import java.util.ArrayList;
import java.util.List;

public class Conta {
    private Integer numero;
    private Integer agencia;
    protected Double saldo;
    private Cliente titular;

    List<Transacao> transacoes = new ArrayList<>();

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
        if (valor != null && valor > 0) {
            saldo += valor;
            adicionarTransacao(valor, TipoTransacao.DEPOSITO, "Depósito");
        }
    }

    public boolean sacar(Double valor) {
        if (valor != null && valor > 0 && saldo >= valor) {
            saldo -= valor;
            adicionarTransacao(valor, TipoTransacao.SAQUE, "Saque");
            return true;
        }
        return false;
    }

    public boolean transferir(Double valor, Conta destino) {
        if (destino != null && valor != null && valor > 0) {
            boolean sacou = this.sacar(valor); 
            if (sacou) {
                destino.depositar(valor);
                adicionarTransacao(valor, TipoTransacao.TRANSFERENCIA,
                        "Transferência para " + destino.getTitular().getNome());
                return true;
            }
        }
        return false;

    }

    public void adicionarTransacao(Double valor, TipoTransacao tipo, String desc) {
        Transacao novaTransacao = new Transacao(valor, tipo, desc);
        transacoes.add(novaTransacao);
    }

    public void imprimirExtrato() {
        for (Transacao t : transacoes) {
            System.out.println(t);
        }
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
