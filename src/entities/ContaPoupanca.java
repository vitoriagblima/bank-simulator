package entities;

public final class ContaPoupanca extends Conta {
    private double taxaRendimento;

    public ContaPoupanca() {
        super();
    }

    public ContaPoupanca(Integer numero, Integer agencia, Cliente cliente, double taxaRendimento) {
        super(numero, agencia, cliente);
        this.taxaRendimento = taxaRendimento;
    }

    @Override
    public boolean sacar(Double valor) {
        if (valor != null && valor > 0 && saldo >= valor) {
            saldo -= valor;
            adicionarTransacao(valor, TipoTransacao.SAQUE, "Saque Conta Poupança");
            return true;
        }
        return false;
    }

    public void renderJuros() {
        if (taxaRendimento > 0) {
            double rendimento = saldo * taxaRendimento;
            saldo += rendimento;
            adicionarTransacao(rendimento, TipoTransacao.RENDIMENTO, "Aplicação de rendimento");
        }
    }

    public double getTaxaRendimento() {
        return this.taxaRendimento;
    }

    public String toString() {
        return "ContaPoupanca {" +
                "numero =" + getNumero() +
                ", agencia =" + getAgencia() +
                ", saldo=R$ " + String.format("%.2f", getSaldo()) +
                ", titular =" + getTitular().getNome() +
                ", taxaRendimento =" + taxaRendimento +
                '}';
    }

}
