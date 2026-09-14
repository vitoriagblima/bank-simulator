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

    public void renderJuros() {
        if (taxaRendimento == null || taxaRendimento.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("A taxa de rendimento é inválida para aplicação de juros.");
        }
        if (saldo.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DomainException("Não é possível aplicar rendimentos em uma conta sem saldo positivo.");
        }
        BigDecimal valorRendimentoBruto = normalizarTaxa(saldo.multiply(taxaRendimento));
        BigDecimal valorRendimentoFinal = normalizar(valorRendimentoBruto);
        saldo = normalizar(saldo.add(valorRendimentoFinal));
        adicionarTransacao(valorRendimentoFinal, TipoTransacao.RENDIMENTO, "Aplicação de rendimento");
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
        if (taxaRendimento.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValorInvalidoException("A taxa de rendimento deve ser maior que zero.");
        }
        this.taxaRendimento = normalizarTaxa(taxaRendimento);
    }

    @Override
    public String toString() {
        return "Conta Poupança | Número: " + getNumero() +
                " | Agência: " + getAgencia() +
                " | Saldo: R$ " + String.format("%.2f", getSaldo()) +
                " | Titular: " + getCliente().getNome() +
                " | Taxa Rendimento: " + String.format("%.4f", getTaxaRendimento());
    }

}
