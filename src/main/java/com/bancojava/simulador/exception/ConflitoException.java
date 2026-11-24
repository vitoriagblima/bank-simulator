package com.bancojava.simulador.exception;

public class ConflitoException extends DomainException {

    private static final long serialVersionUID = 1L;

    public ConflitoException(String msg) {
        super(msg);
    }
}