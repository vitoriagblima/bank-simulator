package model.entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.exceptions.SaldoInsuficienteException;
import model.exceptions.ValorInvalidoException;
import model.util.MonetarioConfig;

public abstract class Conta {

    private Long id;
    private Integer numero;
    private Integer agencia;
    private BigDecimal saldo;
    private Cliente cliente;

    private final List<Transacao> transacoes = new ArrayList<>();

    public Conta() {
        this.saldo = normalizar(BigDecimal.ZERO);
    }

    public Conta(Integer numero, Integer agencia, Cliente cliente) {
        if (numero == null || numero <= 0) {
            throw new ValorInvalidoException("O número da conta não pode estar vazio e deve ser maior que zero.");
        }
        if (agencia == null || agencia <= 0) {
            throw new ValorInvalidoException("A agência da conta deve ser maior que zero.");
        }
        if (cliente == null) {
            throw new ValorInvalidoException("O cliente/titular da conta não pode ser nulo.");
        }
        this.numero = numero;
        this.agencia = agencia;
        this.cliente = cliente;
        this.saldo = normalizar(BigDecimal.ZERO);
    }

    // ===================== Operações públicas =====================

    public void depositar(BigDecimal valor) {
        creditar(valor, TipoTransacao.DEPOSITO, "Depósito");
    }

    public void sacar(BigDecimal valor) {
        debitar(valor, TipoTransacao.SAQUE, "Saque");
    }

    public void transferir(BigDecimal valor, Conta destino) {
        if (destino == null) {
            throw new ValorInvalidoException("A conta de destino não pode ser nula.");
        }
        if (destino == this) {
            throw new ValorInvalidoException("Não é possível transferir para a própria conta.");
        }
        validarValor(valor);

        debitar(valor, TipoTransacao.TRANSFERENCIA, "Transferência para " + destino.getCliente().getNome());
        destino.creditar(valor, TipoTransacao.TRANSFERENCIA_RECEBIDA,
                "Transferência recebida de " + this.cliente.getNome());
    }

    // ===================== Núcleo das operações =====================
    // Protegidos: só a própria hierarquia de contas pode movimentar saldo diretamente.

    protected void creditar(BigDecimal valor, TipoTransacao tipo, String descricao) {
        validarValor(valor);
        this.saldo = normalizar(this.saldo.add(valor));
        adicionarTransacao(valor, tipo, descricao);
    }

    protected void debitar(BigDecimal valor, TipoTransacao tipo, String descricao) {
        validarValor(valor);
        if (valor.compareTo(saldoDisponivel()) > 0) {
            throw new SaldoInsuficienteException(mensagemSaldoInsuficiente());
        }
        this.saldo = normalizar(this.saldo.subtract(valor));
        adicionarTransacao(valor, tipo, descricao);
    }

    /** Valor que pode ser sacado. Subclasses podem ampliar (ex.: limite especial). */
    protected BigDecimal saldoDisponivel() {
        return this.saldo;
    }

    protected String mensagemSaldoInsuficiente() {
        return "Saldo insuficiente para realizar a operação.";
    }

    protected void adicionarTransacao(BigDecimal valor, TipoTransacao tipo, String descricao) {
        transacoes.add(new Transacao(normalizar(valor), tipo, descricao));
    }

    private static void validarValor(BigDecimal valor) {
        if (valor == null) {
            throw new ValorInvalidoException("O valor da operação não pode ser nulo.");
        }
        if (valor.signum() <= 0) {
            throw new ValorInvalidoException("O valor da operação deve ser maior que zero.");
        }
    }

    public static BigDecimal normalizar(BigDecimal valor) {
        return valor.setScale(MonetarioConfig.SCALE, MonetarioConfig.ROUNDING_MODE);
    }

    // ===================== Consultas =====================

    public List<Transacao> getTransacoes() {
        return Collections.unmodifiableList(transacoes);
    }

    public void imprimirExtrato() {
        if (transacoes.isEmpty()) {
            System.out.println("Nenhuma movimentação registrada.");
            return;
        }
        transacoes.forEach(System.out::println);
    }

    public abstract String getTipoConta();

    public Long getId() {
        return this.id;
    }

    public Integer getNumero() {
        return this.numero;
    }

    public Integer getAgencia() {
        return this.agencia;
    }

    public BigDecimal getSaldo() {
        return this.saldo;
    }

    public Cliente getCliente() {
        return this.cliente;
    }

    @Override
    public String toString() {
        return getTipoConta() +
                " | Número: " + numero +
                " | Agência: " + agencia +
                " | Saldo: " + MonetarioConfig.formatar(saldo) +
                " | Titular: " + cliente.getNome();
    }
}