package model.entities;

import java.math.BigDecimal;

import model.exceptions.DomainException;
import model.exceptions.ValorInvalidoException;
import model.util.MonetarioConfig;

public final class ContaPoupanca extends Conta {

    private BigDecimal taxaRendimento;

    public ContaPoupanca() {
        super();
        this.taxaRendimento = normalizarTaxa(BigDecimal.ZERO);
    }

    public ContaPoupanca(Integer numero, Integer agencia, Cliente cliente, BigDecimal taxaRendimento) {
        super(numero, agencia, cliente);
        setTaxaRendimento(taxaRendimento);
    }

    @Override
    public String getTipoConta() {
        return "Conta Poupança";
    }

    public void renderJuros() {
        if (taxaRendimento == null || taxaRendimento.signum() <= 0) {
            throw new ValorInvalidoException("A taxa de rendimento é inválida para aplicação de juros.");
        }
        if (getSaldo().signum() <= 0) {
            throw new DomainException("Não é possível aplicar rendimentos em uma conta sem saldo positivo.");
        }
        // Arredondamento único: saldo x taxa é arredondado direto para centavos.
        // (Antes, arredondava para 4 casas e depois para 2, o que gerava erro de arredondamento.)
        BigDecimal rendimento = getSaldo()
                .multiply(taxaRendimento)
                .setScale(MonetarioConfig.SCALE, MonetarioConfig.ROUNDING_MODE);

        if (rendimento.signum() == 0) {
            return; // rendimento menor que um centavo: nada a creditar
        }
        creditar(rendimento, TipoTransacao.RENDIMENTO, "Aplicação de rendimento");
    }

    public static BigDecimal normalizarTaxa(BigDecimal valor) {
        return valor.setScale(MonetarioConfig.SCALE_TAXA, MonetarioConfig.ROUNDING_MODE);
    }

    public BigDecimal getTaxaRendimento() {
        return this.taxaRendimento;
    }

    public void setTaxaRendimento(BigDecimal taxaRendimento) {
        if (taxaRendimento == null) {
            throw new ValorInvalidoException("A taxa de rendimento não pode ser nula.");
        }
        if (taxaRendimento.signum() <= 0) {
            throw new ValorInvalidoException("A taxa de rendimento deve ser maior que zero.");
        }
        this.taxaRendimento = normalizarTaxa(taxaRendimento);
    }

    @Override
    public String toString() {
        return super.toString() +
                " | Taxa Rendimento: " + MonetarioConfig.formatarPercentual(taxaRendimento);
    }
}