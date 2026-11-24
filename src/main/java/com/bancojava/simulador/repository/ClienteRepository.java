package com.bancojava.simulador.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bancojava.simulador.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByDocumento(String documento);
}