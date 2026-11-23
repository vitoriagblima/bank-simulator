package model.entities;

import model.exceptions.ValorInvalidoException;

public class Cliente {

    private Long id;
    private String nome;
    private String documento;
    private TipoCliente tipoCliente;

    public Cliente() {
    }

    public Cliente(String nome, String documento, TipoCliente tipoCliente) {
        setNome(nome);
        setDocumento(documento);
        setTipoCliente(tipoCliente);
    }

    public Long getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new ValorInvalidoException("O nome do cliente não pode ser nulo ou vazio.");
        }
        this.nome = nome.trim();
    }

    public String getDocumento() {
        return this.documento;
    }

    public void setDocumento(String documento) {
        if (documento == null || documento.isBlank()) {
            throw new ValorInvalidoException("O documento do cliente não pode ser nulo ou vazio.");
        }
        this.documento = documento.trim();
    }

    public TipoCliente getTipoCliente() {
        return this.tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        if (tipoCliente == null) {
            throw new ValorInvalidoException("O tipo do cliente deve ser preenchido.");
        }
        this.tipoCliente = tipoCliente;
    }

    @Override
    public String toString() {
        return "Cliente: " + nome + " | Documento: " + documento + " | Tipo: " + tipoCliente;
    }
}