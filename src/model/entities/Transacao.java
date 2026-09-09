package model.entities;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Transacao {

    private Long id;
    private ZonedDateTime dataHora;
    private BigDecimal valor;
    private TipoTransacao tipo;
    private String descricao;

    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

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

    public String toString() {
        String dataFormatada = this.dataHora.format(FORMATADOR);
        return "Transacao: " + this.tipo + " | Data hora: " + dataFormatada + " | Valor: " +
                String.format("%.2f", getValor()) + " | Descricao: " + this.descricao;
    }
}
