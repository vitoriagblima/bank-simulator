package entities;

public final class ContaCorrente extends Conta {
    private Double limiteEspecial;
    private Double taxaManutencao;

    public ContaCorrente() {
        super();
    }

    public ContaCorrente(Integer numero, Integer agencia, Cliente cliente, Double limiteEspecial,
            Double taxaManutencao) {
        super(numero, agencia, cliente);
        this.limiteEspecial = limiteEspecial;
        this.taxaManutencao = taxaManutencao;
    }

    @Override
    public boolean sacar(Double valor) {
        if (valor != null && valor > 0 && (saldo + limiteEspecial) >= valor) {
            saldo -= valor;
            adicionarTransacao(valor, TipoTransacao.SAQUE, "Saque Conta Corrente");
            return true;
        }
        return false;
    }

    public void cobrarTaxaManutencao() {
        if (taxaManutencao != null && (saldo + limiteEspecial) >= taxaManutencao) {
            saldo -= taxaManutencao;
            adicionarTransacao(taxaManutencao, TipoTransacao.TAXA_MANUTENCAO, "Cobrança de taxa de manutenção");
        }
    }

    public Double getLimiteEspecial() {
        return this.limiteEspecial;
    }

    public Double getTaxaManutencao() {
        return this.taxaManutencao;
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
