package com.bancojava.simulador.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bancojava.simulador.dto.ContaDto;
import com.bancojava.simulador.exception.ConflitoException;
import com.bancojava.simulador.exception.RecursoNaoEncontradoException;
import com.bancojava.simulador.exception.ValorInvalidoException;
import com.bancojava.simulador.model.Cliente;
import com.bancojava.simulador.model.Conta;
import com.bancojava.simulador.model.ContaCorrente;
import com.bancojava.simulador.model.ContaPoupanca;
import com.bancojava.simulador.repository.ClienteRepository;
import com.bancojava.simulador.repository.ContaRepository;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;

    public ContaService(ContaRepository contaRepository, ClienteRepository clienteRepository) {
        this.contaRepository = contaRepository;
        this.clienteRepository = clienteRepository;
    }

    // ===================== Abertura de contas =====================

    @Transactional
    public ContaDto.Response abrirCorrente(ContaDto.CorrenteRequest request) {
        garantirNumeroLivre(request.numero());
        Cliente cliente = buscarCliente(request.clienteId());
        ContaCorrente conta = new ContaCorrente(request.numero(), request.agencia(), cliente,
                request.limiteEspecial(), request.taxaManutencao());
        return ContaDto.Response.from(contaRepository.save(conta));
    }

    @Transactional
    public ContaDto.Response abrirPoupanca(ContaDto.PoupancaRequest request) {
        garantirNumeroLivre(request.numero());
        Cliente cliente = buscarCliente(request.clienteId());
        ContaPoupanca conta = new ContaPoupanca(request.numero(), request.agencia(), cliente,
                request.taxaRendimento());
        return ContaDto.Response.from(contaRepository.save(conta));
    }

    // ===================== Consultas =====================

    @Transactional(readOnly = true)
    public List<ContaDto.Response> listar() {
        return contaRepository.findAll().stream()
                .map(ContaDto.Response::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContaDto.Response buscar(Integer numero) {
        return ContaDto.Response.from(buscarConta(numero));
    }

    @Transactional(readOnly = true)
    public List<ContaDto.Movimento> extrato(Integer numero) {
        return buscarConta(numero).getTransacoes().stream()
                .map(ContaDto.Movimento::from)
                .toList();
    }

    // ===================== Movimentações =====================

    @Transactional
    public ContaDto.Response depositar(Integer numero, BigDecimal valor) {
        Conta conta = buscarConta(numero);
        conta.depositar(valor);
        return ContaDto.Response.from(conta);
    }

    @Transactional
    public ContaDto.Response sacar(Integer numero, BigDecimal valor) {
        Conta conta = buscarConta(numero);
        conta.sacar(valor);
        return ContaDto.Response.from(conta);
    }

    /** Transfere da conta de {@code numeroOrigem} para a conta destino, na mesma transação. */
    @Transactional
    public ContaDto.Response transferir(Integer numeroOrigem, ContaDto.Transferencia request) {
        Conta origem = buscarConta(numeroOrigem);
        Conta destino = buscarConta(request.numeroDestino());
        origem.transferir(request.valor(), destino);
        return ContaDto.Response.from(origem);
    }

    @Transactional
    public ContaDto.Response aplicarRendimento(Integer numero) {
        Conta conta = buscarConta(numero);
        if (!(conta instanceof ContaPoupanca poupanca)) {
            throw new ValorInvalidoException("Rendimento só pode ser aplicado em conta poupança.");
        }
        poupanca.renderJuros();
        return ContaDto.Response.from(poupanca);
    }

    @Transactional
    public ContaDto.Response cobrarTaxaManutencao(Integer numero) {
        Conta conta = buscarConta(numero);
        if (!(conta instanceof ContaCorrente corrente)) {
            throw new ValorInvalidoException("Taxa de manutenção só pode ser cobrada em conta corrente.");
        }
        corrente.cobrarTaxaManutencao();
        return ContaDto.Response.from(corrente);
    }

    // ===================== Auxiliares =====================

    private Conta buscarConta(Integer numero) {
        return contaRepository.findByNumero(numero)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Conta com o número " + numero + " não foi encontrada."));
    }

    private Cliente buscarCliente(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Cliente com o id " + clienteId + " não foi encontrado."));
    }

    private void garantirNumeroLivre(Integer numero) {
        if (contaRepository.existsByNumero(numero)) {
            throw new ConflitoException("Já existe uma conta cadastrada com o número " + numero + ".");
        }
    }
}