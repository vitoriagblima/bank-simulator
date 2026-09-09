package model.entities;

import java.math.BigDecimal;

import model.exceptions.DomainException;
import model.exceptions.SaldoInsuficienteException;
import model.exceptions.ValorInvalidoException;

public final class ContaPoupanca extends Conta {
    private BigDecimal taxaRendimento;

    public ContaPoupanca() {
        super();
        this.taxaRendimento = BigDecimal.ZERO;
    }

    public ContaPoupanca(Integer numero, Integer agencia, Cliente cliente, BigDecimal taxaRendimento) {
        super(numero, agencia, cliente);
        setTaxaRendimento(taxaRendimento);
    }

    @Override
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
        saldo = saldo.subtract(valor);
        adicionarTransacao(valor, tipo, descricao);
    }

    public void renderJuros() {
        if (taxaRendimento == null || taxaRendimento.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("A taxa de rendimento é inválida para aplicação de juros.");
        }
        if (saldo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Não é possível aplicar rendimentos em uma conta sem saldo positivo.");
        }
        BigDecimal rendimento = saldo.multiply(taxaRendimento);
        saldo = saldo.add(rendimento);
        adicionarTransacao(rendimento, TipoTransacao.RENDIMENTO, "Aplicação de rendimento");
    }

    public BigDecimal getTaxaRendimento() {
        return this.taxaRendimento;
    }

    public void setTaxaRendimento(BigDecimal taxaRendimento) {
        if (taxaRendimento == null) {
            throw new ValorInvalidoException("A taxa de rendimento não pode ser nula.");
        }
        if (taxaRendimento.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("A taxa de rendimento deve ser maior que zero.");
        }
        this.taxaRendimento = taxaRendimento;
    }

    @Override
    public String toString() {
        return "Conta Poupança | Número: " + getNumero() +
                " | Agência: " + getAgencia() +
                " | Saldo: R$ " + String.format("%.2f", getSaldo()) +
                " | Titular: " + getTitular().getNome() +
                " | Taxa Rendimento: " + String.format("%.2f", getTaxaRendimento());
    }

}
