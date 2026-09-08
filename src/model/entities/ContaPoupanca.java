package model.entities;

import model.exceptions.DomainException;
import model.exceptions.SaldoInsuficienteException;
import model.exceptions.ValorInvalidoException;

public final class ContaPoupanca extends Conta {
    private Double taxaRendimento;

    public ContaPoupanca() {
        super();
    }

    public ContaPoupanca(Integer numero, Integer agencia, Cliente cliente, double taxaRendimento) {
        super(numero, agencia, cliente);
        this.taxaRendimento = taxaRendimento;
    }

    @Override
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
        saldo -= valor;
        adicionarTransacao(valor, tipo, descricao);
    }

    public void renderJuros() {
        if (taxaRendimento == null || taxaRendimento <= 0) {
            throw new ValorInvalidoException("A taxa de rendimento é inválida para aplicação de juros.");
        }
        if (saldo <= 0) {
            throw new DomainException("Não é possível aplicar rendimentos em uma conta sem saldo positivo.");
        }
        double rendimento = saldo * taxaRendimento;
        saldo += rendimento;
        adicionarTransacao(rendimento, TipoTransacao.RENDIMENTO, "Aplicação de rendimento");
    }

    public double getTaxaRendimento() {
        return this.taxaRendimento;
    }

    public void setTaxaRendimento(double taxaRendimento) {
        this.taxaRendimento = taxaRendimento;
    }

    @Override
    public String toString() {
        return "Conta Poupança | Número: " + getNumero() +
                " | Agência: " + getAgencia() +
                " | Saldo: R$ " + String.format("%.2f", getSaldo()) +
                " | Titular: " + getTitular().getNome() +
                " | Taxa Rendimento: " + taxaRendimento;
    }

}
