package model.entities;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import model.util.MonetarioConfig;

public class Transacao {

    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private Long id;
    private ZonedDateTime dataHora;
    private BigDecimal valor;
    private TipoTransacao tipo;
    private String descricao;

    public Transacao() {
        this.dataHora = ZonedDateTime.now(ZoneId.systemDefault());
    }

    public Transacao(BigDecimal valor, TipoTransacao tipo, String descricao) {
        this.valor = valor;
        this.tipo = tipo;
        this.descricao = descricao;
        this.dataHora = ZonedDateTime.now(ZoneId.systemDefault());
    }

    public Long getId() {
        return this.id;
    }

    public ZonedDateTime getDataHora() {
        return this.dataHora;
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public TipoTransacao getTipo() {
        return this.tipo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    @Override
    public String toString() {
        return "Transação: " + tipo +
                " | Data/hora: " + dataHora.format(FORMATADOR) +
                " | Valor: " + MonetarioConfig.formatar(valor) +
                " | Descrição: " + descricao;
    }
}