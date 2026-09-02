package entities;

import java.time.LocalDateTime;

public class Transacao {

    private LocalDateTime dataHora;
    private Double valor;
    private TipoTransacao tipo;
    private String descricao;

    public Transacao() {
    }

    public Transacao(Double valor, TipoTransacao tipo, String descricao) {
        this.valor = valor;
        this.tipo = tipo;
        this.descricao = descricao;
    }

    public LocalDateTime getDataHora() {
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
        return "Transacao: " + this.tipo + " | Valor: " 
        + this.valor + " | Descricao: " + this.descricao;
    }
}
