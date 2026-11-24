package com.bancojava.simulador.exception;

public class ValorInvalidoException extends DomainException {

    private static final long serialVersionUID = 1L;

    public ValorInvalidoException(String msg) {
        super(msg);
    }
}