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
        return "ContaCorrente {" +
                "numero =" + getNumero() +
                ", agencia =" + getAgencia() +
                ", saldo =" + getSaldo() +
                ", titular =" + getTitular() +
                ", limiteEspecial =" + getLimiteEspecial() +
                ", taxaManutencao =" + getTaxaManutencao() +
                '}';
    }
}
