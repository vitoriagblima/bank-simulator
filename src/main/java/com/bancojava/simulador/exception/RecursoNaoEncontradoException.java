package com.bancojava.simulador.exception;

public class RecursoNaoEncontradoException extends DomainException {

    private static final long serialVersionUID = 1L;

    public RecursoNaoEncontradoException(String msg) {
        super(msg);
    }
}