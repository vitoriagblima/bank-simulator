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
        if (nome == null || nome.trim().isEmpty()){
            throw new ValorInvalidoException("O nome do cliente não pode ser nulo ou vazio.");
        }
        this.nome = nome;
    }

    public String getDocumento() {
        return this.documento;
    }

    public void setDocumento(String documento) {
        if (documento == null || documento.trim().isEmpty()){
            throw new ValorInvalidoException("O documento do cliente não pode ser nulo ou vazio.");
        }
        this.documento = documento;
    }

    public TipoCliente getTipoCliente() {
        return this.tipoCliente;
    }

    public void setTipoCliente(TipoCliente tipoCliente) {
        if (tipoCliente == null){
            throw new ValorInvalidoException("O tipo do cliente deve ser preenchido.");
        }
        this.tipoCliente = tipoCliente;
    }

    public String toString() {
        return "Cliente: " + this.nome + " | Documento: " 
        + this.documento + " | Tipo: " + this.tipoCliente;
    }
}
