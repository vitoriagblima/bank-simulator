
package com.bancojava.simulador.controller;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.bancojava.simulador.exception.ConflitoException;
import com.bancojava.simulador.exception.DomainException;
import com.bancojava.simulador.exception.RecursoNaoEncontradoException;
import com.bancojava.simulador.exception.SaldoInsuficienteException;
import com.bancojava.simulador.exception.ValorInvalidoException;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ProblemDetail> naoEncontrado(
            RecursoNaoEncontradoException ex) {
        return problema(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(ConflitoException.class)
    public ResponseEntity<ProblemDetail> conflito(ConflitoException ex) {
        return problema(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<ProblemDetail> saldoInsuficiente(
            SaldoInsuficienteException ex) {
        return problema(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(ValorInvalidoException.class)
    public ResponseEntity<ProblemDetail> valorInvalido(
            ValorInvalidoException ex) {
        return problema(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ProblemDetail> domain(DomainException ex) {
        return problema(HttpStatus.UNPROCESSABLE_ENTITY, ex.getMessage());
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ProblemDetail> concorrencia(
            OptimisticLockingFailureException ex) {
        return problema(
                HttpStatus.CONFLICT,
                "A conta foi alterada por outra operação simultânea. Tente novamente."
        );
    }

    private ResponseEntity<ProblemDetail> problema(
            HttpStatus status, String detalhe) {

        ProblemDetail problema =
                ProblemDetail.forStatusAndDetail(status, detalhe);

        problema.setTitle(status.getReasonPhrase());

        return ResponseEntity.status(status).body(problema);
    }
}
