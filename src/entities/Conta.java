package entities;

import java.util.ArrayList;
import java.util.List;

public abstract class Conta {
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
        if (valor != null && valor > 0) {
            saldo += valor;
            adicionarTransacao(valor, TipoTransacao.DEPOSITO, "Depósito");
        }
    }

    public boolean sacar(Double valor) {
        return sacar(valor, TipoTransacao.SAQUE, "Saque");
    }

    public boolean sacar(Double valor, TipoTransacao tipo, String descricao) {
        if (valor != null && valor > 0 && saldo >= valor) {
            saldo -= valor;
            adicionarTransacao(valor, tipo, descricao);
            return true;
        }
        return false;
    }

    public boolean transferir(Double valor, Conta destino) {
        if (destino != null && valor != null && valor > 0) {
            String descricao = "Transferência para " + destino.getTitular().getNome();
            
            boolean sacou = this.sacar(valor, TipoTransacao.TRANSFERENCIA, descricao); 
            if (sacou) {
                destino.depositar(valor);
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