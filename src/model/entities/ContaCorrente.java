package model.entities;

import model.exceptions.SaldoInsuficienteException;
import model.exceptions.ValorInvalidoException;

public final class ContaCorrente extends Conta {
    private Double limiteEspecial;
    private Double taxaManutencao;

    public ContaCorrente() {
        super();
        this.limiteEspecial = 0.0;
        this.taxaManutencao = 0.0;
    }

    public ContaCorrente(Integer numero, Integer agencia, Cliente cliente, Double limiteEspecial,
            Double taxaManutencao) {
        super(numero, agencia, cliente);
        setLimiteEspecial(limiteEspecial);
        setTaxaManutencao(taxaManutencao);
    }

    @Override
    public void sacar(Double valor, TipoTransacao tipo, String descricao) {
        if (valor == null) {
            throw new ValorInvalidoException("O valor do saque não pode ser nulo.");
        }
        if (valor <= 0) {
            throw new ValorInvalidoException("O valor do saque deve ser maior que zero.");
        }
        if (valor > saldo + limiteEspecial) {
            throw new SaldoInsuficienteException("Saldo e limite especial insuficientes para realizar o saque.");
        }
        saldo -= valor;
        adicionarTransacao(valor, tipo, descricao);
    }

    public void cobrarTaxaManutencao() {
        if (taxaManutencao == null || taxaManutencao <= 0) {
            throw new ValorInvalidoException("Taxa de manutenção inválida para cobrança.");
        }
        sacar(taxaManutencao, TipoTransacao.TAXA_MANUTENCAO, "Cobrança de taxa de manutenção");
    }

    public Double getLimiteEspecial() {
        return this.limiteEspecial;
    }

    public void setLimiteEspecial(Double limiteEspecial) {
        if (limiteEspecial == null) {
            throw new ValorInvalidoException("O limite especial não pode ser nulo.");
        }
        if (limiteEspecial < 0) {
            throw new ValorInvalidoException("O limite especial não pode ser negativo.");
        }
        this.limiteEspecial = limiteEspecial;
    }
   
    public Double getTaxaManutencao() {
        return this.taxaManutencao;
    }

     public void setTaxaManutencao(Double taxaManutencao) {
        if (taxaManutencao == null) {
            throw new ValorInvalidoException("A taxa de manutenção não pode ser nula.");
        }
        if (taxaManutencao < 0) {
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
