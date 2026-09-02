package entities;

public class Cliente {

    private String nome;
    private String documento;
    private TipoCliente tipoCliente;

    public Cliente() {
    }

    public Cliente(String nome, String documento, TipoCliente tipoCliente) {
        this.nome = nome;
        this.documento = documento;
        this.tipoCliente = tipoCliente;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDocumento() {
        return this.documento;
    }

    public TipoCliente getTipoCliente() {
        return this.tipoCliente;
    }

    public String toString() {
        return "Cliente: " + this.nome + " | Documento: " 
        + this.documento + " | Tipo: " + this.tipoCliente;
    }
}
