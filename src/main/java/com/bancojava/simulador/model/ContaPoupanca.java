
package com.bancojava.simulador.model;

import java.math.BigDecimal;

import com.bancojava.simulador.exception.SaldoInsuficienteException;
import com.bancojava.simulador.exception.ValorInvalidoException;
import com.bancojava.simulador.util.MonetarioConfig;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("POUPANCA")
public class ContaPoupanca extends Conta {

    @Column(name = "taxa_rendimento", precision = 10, scale = 4)
    private BigDecimal taxaRendimento;

    /** Construtor exigido pelo JPA. */
    protected ContaPoupanca() {
        super();
        this.taxaRendimento = normalizarTaxa(BigDecimal.ZERO);
    }

    public ContaPoupanca(
            Integer numero,
            Integer agencia,
            Cliente cliente,
            BigDecimal taxaRendimento) {

        super(numero, agencia, cliente);
        setTaxaRendimento(taxaRendimento);
    }

    @Override
    public String getTipoConta() {
        return "Conta Poupança";
    }

    public void renderJuros() {

        if (taxaRendimento == null || taxaRendimento.signum() <= 0) {
            throw new ValorInvalidoException(
                    "A taxa de rendimento é inválida para aplicação de juros."
            );
        }

        if (getSaldo().signum() <= 0) {
            throw new SaldoInsuficienteException(
                    "Não é possível aplicar rendimentos em uma conta sem saldo positivo."
            );
        }

        // Arredondamento único: saldo x taxa é arredondado direto para centavos.
        BigDecimal rendimento = getSaldo()
                .multiply(taxaRendimento)
                .setScale(
                        MonetarioConfig.SCALE,
                        MonetarioConfig.ROUNDING_MODE
                );

        if (rendimento.signum() == 0) {
            return; // rendimento menor que um centavo: nada a creditar
        }

        creditar(
                rendimento,
                TipoTransacao.RENDIMENTO,
                "Aplicação de rendimento"
        );
    }

    public static BigDecimal normalizarTaxa(BigDecimal valor) {
        return valor.setScale(
                MonetarioConfig.SCALE_TAXA,
                MonetarioConfig.ROUNDING_MODE
        );
    }

    public BigDecimal getTaxaRendimento() {
        return taxaRendimento;
    }

    public void setTaxaRendimento(BigDecimal taxaRendimento) {

        if (taxaRendimento == null) {
            throw new ValorInvalidoException(
                    "A taxa de rendimento não pode ser nula."
            );
        }

        if (taxaRendimento.signum() <= 0) {
            throw new ValorInvalidoException(
                    "A taxa de rendimento deve ser maior que zero."
            );
        }

        this.taxaRendimento = normalizarTaxa(taxaRendimento);
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Taxa Rendimento: "
                + MonetarioConfig.formatarPercentual(taxaRendimento);
    }
}
