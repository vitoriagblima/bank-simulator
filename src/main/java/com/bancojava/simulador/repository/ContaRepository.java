package com.bancojava.simulador.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bancojava.simulador.model.Conta;

public interface ContaRepository extends JpaRepository<Conta, Long> {

    Optional<Conta> findByNumero(Integer numero);

    boolean existsByNumero(Integer numero);
}