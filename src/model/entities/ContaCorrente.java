package model.entities;

import java.math.BigDecimal;

import model.exceptions.SaldoInsuficienteException;
import model.exceptions.ValorInvalidoException;

public final class ContaCorrente extends Conta {
    private BigDecimal limiteEspecial;
    private BigDecimal taxaManutencao;

    public ContaCorrente() {
        super();
        this.limiteEspecial = BigDecimal.ZERO;
        this.taxaManutencao = BigDecimal.ZERO;
    }

    public ContaCorrente(Integer numero, Integer agencia, Cliente cliente, BigDecimal limiteEspecial,
            BigDecimal taxaManutencao) {
        super(numero, agencia, cliente);
        setLimiteEspecial(limiteEspecial);
        setTaxaManutencao(taxaManutencao);
    }

    @Override
    public void sacar(BigDecimal valor, TipoTransacao tipo, String descricao) {
        if (valor == null) {
            throw new ValorInvalidoException("O valor do saque não pode ser nulo.");
        }
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("O valor do saque deve ser maior que zero.");
        }
        if (valor.compareTo(saldo.add(limiteEspecial)) > 0) {
            throw new SaldoInsuficienteException("Saldo e limite especial insuficientes para realizar o saque.");
        }
        saldo = saldo.subtract(valor);
        adicionarTransacao(valor, tipo, descricao);
    }

    public void cobrarTaxaManutencao() {
        if (taxaManutencao == null || taxaManutencao.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("Taxa de manutenção inválida para cobrança.");
        }
        sacar(taxaManutencao, TipoTransacao.TAXA_MANUTENCAO, "Cobrança de taxa de manutenção");
    }

    public BigDecimal getLimiteEspecial() {
        return this.limiteEspecial;
    }

    public void setLimiteEspecial(BigDecimal limiteEspecial) {
        if (limiteEspecial == null) {
            throw new ValorInvalidoException("O limite especial não pode ser nulo.");
        }
        if (limiteEspecial.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValorInvalidoException("O limite especial não pode ser negativo.");
        }
        this.limiteEspecial = limiteEspecial;
    }
   
    public BigDecimal getTaxaManutencao() {
        return this.taxaManutencao;
    }

     public void setTaxaManutencao(BigDecimal taxaManutencao) {
        if (taxaManutencao == null) {
            throw new ValorInvalidoException("A taxa de manutenção não pode ser nula.");
        }
        if (taxaManutencao.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValorInvalidoException("A taxa de manutenção não pode ser negativa.");
        }
        this.taxaManutencao = taxaManutencao;
    }

    @Override
    public String toString() {
        return "Conta Corrente | Número: " + getNumero() +
                " | Agência: " + getAgencia() +
                " | Saldo: R$ " + String.format("%.2f", getSaldo()) +
                " | Titular: " + getTitular().getNome() +
                " | Limite Especial: R$ " + String.format("%.2f", getLimiteEspecial()) +
                " | Taxa Manutenção: R$ " + String.format("%.2f", getTaxaManutencao());
    }
}
