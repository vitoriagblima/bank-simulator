package com.bancojava.simulador.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bancojava.simulador.dto.ClienteDto;
import com.bancojava.simulador.service.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDto.Response cadastrar(@Valid @RequestBody ClienteDto.Request request) {
        return clienteService.cadastrar(request);
    }

    @GetMapping("/{id}")
    public ClienteDto.Response buscar(@PathVariable Long id) {
        return clienteService.buscar(id);
    }
}