package com.bancojava.simulador.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.bancojava.simulador.exception.SaldoInsuficienteException;
import com.bancojava.simulador.exception.ValorInvalidoException;
import com.bancojava.simulador.util.MonetarioConfig;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "contas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_conta", discriminatorType = DiscriminatorType.STRING)
public abstract class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer numero;

    @Column(nullable = false)
    private Integer agencia;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal saldo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<Transacao> transacoes = new ArrayList<>();

    /** Controle de concorrência otimista: evita perda de atualização em operações simultâneas. */
    @Version
    private Long versao;

    /** Construtor exigido pelo JPA. */
    protected Conta() {
        this.saldo = normalizar(BigDecimal.ZERO);
    }

    protected Conta(Integer numero, Integer agencia, Cliente cliente) {
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
        if (destino == this || destino.getNumero().equals(this.numero)) {
            throw new ValorInvalidoException("Não é possível transferir para a própria conta.");
        }
        validarValor(valor);

        debitar(valor, TipoTransacao.TRANSFERENCIA, "Transferência para " + destino.getCliente().getNome());
        destino.creditar(valor, TipoTransacao.TRANSFERENCIA_RECEBIDA,
                "Transferência recebida de " + this.cliente.getNome());
    }

    // ===================== Núcleo das operações =====================
    // Protegidos: só a própria hierarquia de contas movimenta saldo diretamente.

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
        transacoes.add(new Transacao(this, normalizar(valor), tipo, descricao));
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

    public abstract String getTipoConta();

    public Long getId() {
        return id;
    }

    public Integer getNumero() {
        return numero;
    }

    public Integer getAgencia() {
        return agencia;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public Cliente getCliente() {
        return cliente;
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