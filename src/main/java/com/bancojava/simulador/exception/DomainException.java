package com.bancojava.simulador.exception;

/** Erro de regra de negócio. Subclasses indicam o tipo de falha para o tratamento HTTP. */
public class DomainException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DomainException(String msg) {
        super(msg);
    }
}