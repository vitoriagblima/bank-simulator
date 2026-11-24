package com.bancojava.simulador.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bancojava.simulador.dto.ClienteDto;
import com.bancojava.simulador.exception.ConflitoException;
import com.bancojava.simulador.exception.RecursoNaoEncontradoException;
import com.bancojava.simulador.model.Cliente;
import com.bancojava.simulador.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public ClienteDto.Response cadastrar(ClienteDto.Request request) {
        String documento = request.documento().trim();
        if (clienteRepository.existsByDocumento(documento)) {
            throw new ConflitoException("Já existe um cliente cadastrado com o documento " + documento + ".");
        }
        Cliente cliente = clienteRepository.save(
                new Cliente(request.nome(), documento, request.tipo()));
        return ClienteDto.Response.from(cliente);
    }

    @Transactional(readOnly = true)
    public ClienteDto.Response buscar(Long id) {
        return clienteRepository.findById(id)
                .map(ClienteDto.Response::from)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente com o id " + id + " não foi encontrado."));
    }
}