package entities;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Transacao {

    private ZonedDateTime dataHora;
    private Double valor;
    private TipoTransacao tipo;
    private String descricao;

    private static final DateTimeFormatter FORMATADOR = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public Transacao() {
        this.dataHora = ZonedDateTime.now(ZoneId.systemDefault());
    }

    public Transacao(Double valor, TipoTransacao tipo, String descricao) {
        this.valor = valor;
        this.tipo = tipo;
        this.descricao = descricao;
        this.dataHora = ZonedDateTime.now(ZoneId.systemDefault());
    }

    public ZonedDateTime getDataHora() {
        return this.dataHora;
    }

    public Double getValor() {
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
        return "Transacao: " + this.tipo + " | Data hora: " + dataFormatada + " | Valor: " + this.valor
                + " | Descricao: " + this.descricao;
    }
}
