package com.bancojava.simulador.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bancojava.simulador.dto.ContaDto;
import com.bancojava.simulador.service.ContaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    // ----------------------------- Cadastro -----------------------------

    @PostMapping("/corrente")
    @ResponseStatus(HttpStatus.CREATED)
    public ContaDto.Response abrirCorrente(@Valid @RequestBody ContaDto.CorrenteRequest request) {
        return contaService.abrirCorrente(request);
    }

    @PostMapping("/poupanca")
    @ResponseStatus(HttpStatus.CREATED)
    public ContaDto.Response abrirPoupanca(@Valid @RequestBody ContaDto.PoupancaRequest request) {
        return contaService.abrirPoupanca(request);
    }

    // ----------------------------- Consultas -----------------------------

    @GetMapping
    public List<ContaDto.Response> listar() {
        return contaService.listar();
    }

    @GetMapping("/{numero}")
    public ContaDto.Response buscar(@PathVariable Integer numero) {
        return contaService.buscar(numero);
    }

    @GetMapping("/{numero}/extrato")
    public List<ContaDto.Movimento> extrato(@PathVariable Integer numero) {
        return contaService.extrato(numero);
    }

    // ----------------------------- Operações -----------------------------

    @PostMapping("/{numero}/depositos")
    public ContaDto.Response depositar(@PathVariable Integer numero,
                                       @Valid @RequestBody ContaDto.Operacao request) {
        return contaService.depositar(numero, request.valor());
    }

    @PostMapping("/{numero}/saques")
    public ContaDto.Response sacar(@PathVariable Integer numero,
                                   @Valid @RequestBody ContaDto.Operacao request) {
        return contaService.sacar(numero, request.valor());
    }

    @PostMapping("/{numero}/transferencias")
    public ContaDto.Response transferir(@PathVariable Integer numero,
                                        @Valid @RequestBody ContaDto.Transferencia request) {
        return contaService.transferir(numero, request);
    }

    @PostMapping("/{numero}/rendimentos")
    public ContaDto.Response aplicarRendimento(@PathVariable Integer numero) {
        return contaService.aplicarRendimento(numero);
    }

    @PostMapping("/{numero}/taxa-manutencao")
    public ContaDto.Response cobrarTaxaManutencao(@PathVariable Integer numero) {
        return contaService.cobrarTaxaManutencao(numero);
    }
}