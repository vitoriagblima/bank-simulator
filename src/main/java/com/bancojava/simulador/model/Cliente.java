package com.bancojava.simulador.model;

import com.bancojava.simulador.exception.ValorInvalidoException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, unique = true, length = 20)
    private String documento;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cliente", nullable = false, length = 20)
    private TipoCliente tipoCliente;

    /** Construtor exigido pelo JPA. */
    protected Cliente() {
    }

    public Cliente(String nome, String documento, TipoCliente tipoCliente) {
        setNome(nome);
        setDocumento(documento);
        setTipoCliente(tipoCliente);
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new ValorInvalidoException("O nome do cliente não pode ser nulo ou vazio.");
        }
        this.nome = nome.trim();
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        if (documento == null || documento.isBlank()) {
            throw new ValorInvalidoException("O documento do cliente não pode ser nulo ou vazio.");
        }
        this.documento = documento.trim();
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
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