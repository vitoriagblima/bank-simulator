package com.bancojava.simulador.exception;

public class SaldoInsuficienteException extends DomainException {

    private static final long serialVersionUID = 1L;

    public SaldoInsuficienteException(String msg) {
        super(msg);
    }
}