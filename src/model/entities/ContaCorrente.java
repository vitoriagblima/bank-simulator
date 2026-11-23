package model.entities;

import java.math.BigDecimal;

import model.exceptions.ValorInvalidoException;
import model.util.MonetarioConfig;

public final class ContaCorrente extends Conta {

    private BigDecimal limiteEspecial;
    private BigDecimal taxaManutencao;

    public ContaCorrente() {
        super();
        this.limiteEspecial = normalizar(BigDecimal.ZERO);
        this.taxaManutencao = normalizar(BigDecimal.ZERO);
    }

    public ContaCorrente(Integer numero, Integer agencia, Cliente cliente,
                         BigDecimal limiteEspecial, BigDecimal taxaManutencao) {
        super(numero, agencia, cliente);
        setLimiteEspecial(limiteEspecial);
        setTaxaManutencao(taxaManutencao);
    }

    @Override
    public String getTipoConta() {
        return "Conta Corrente";
    }

    /** Na conta corrente, o disponível é saldo + limite especial. */
    @Override
    protected BigDecimal saldoDisponivel() {
        return getSaldo().add(limiteEspecial);
    }

    @Override
    protected String mensagemSaldoInsuficiente() {
        return "Saldo e limite especial insuficientes para realizar a operação.";
    }

    public void cobrarTaxaManutencao() {
        if (taxaManutencao.signum() <= 0) {
            throw new ValorInvalidoException("Taxa de manutenção inválida para cobrança.");
        }
        debitar(taxaManutencao, TipoTransacao.TAXA_MANUTENCAO, "Cobrança de taxa de manutenção");
    }

    public BigDecimal getLimiteEspecial() {
        return this.limiteEspecial;
    }

    public void setLimiteEspecial(BigDecimal limiteEspecial) {
        if (limiteEspecial == null) {
            throw new ValorInvalidoException("O limite especial não pode ser nulo.");
        }
        if (limiteEspecial.signum() < 0) {
            throw new ValorInvalidoException("O limite especial não pode ser negativo.");
        }
        this.limiteEspecial = normalizar(limiteEspecial);
    }

    public BigDecimal getTaxaManutencao() {
        return this.taxaManutencao;
    }

    public void setTaxaManutencao(BigDecimal taxaManutencao) {
        if (taxaManutencao == null) {
            throw new ValorInvalidoException("A taxa de manutenção não pode ser nula.");
        }
        if (taxaManutencao.signum() < 0) {
            throw new ValorInvalidoException("A taxa de manutenção não pode ser negativa.");
        }
        this.taxaManutencao = normalizar(taxaManutencao);
    }

    @Override
    public String toString() {
        return super.toString() +
                " | Limite Especial: " + MonetarioConfig.formatar(limiteEspecial) +
                " | Taxa Manutenção: " + MonetarioConfig.formatar(taxaManutencao);
    }
}